package com.gmail.yauhenizhukovich.app.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.OrderRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.repository.model.Order;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.service.OrderService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.CustomerDetailsException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.AddOrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.UpdateOrderDTO;
import com.gmail.yauhenizhukovich.app.service.util.conversion.OrderConversionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.gmail.yauhenizhukovich.app.repository.model.StatusEnum.NEW;
import static com.gmail.yauhenizhukovich.app.service.constant.AuthorityConstant.ANONYMOUS_USER_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_ORDERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getStartPositionByPageNumber;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.OrderConversionUtil.convertDTOToDatabaseObject;
import static com.gmail.yauhenizhukovich.app.service.util.conversion.OrderConversionUtil.convertDatabaseObjectToOrderDTO;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ItemRepository itemRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderDTO addOrder(AddOrderDTO orderDTO) throws CustomerDetailsException {
        Order order = convertDTOToDatabaseObject(orderDTO);
        order.setDate(LocalDate.now());
        Item item = itemRepository.getItemByUniqueNumber(orderDTO.getItemUniqueNumber());
        order.setItem(item);
        order.setStatus(NEW);
        checkAndSetUserDetails(order);
        BigDecimal totalPrice = BigDecimal.valueOf(order.getAmount()).multiply(item.getPrice());
        order.setPrice(totalPrice);
        order = orderRepository.add(order);
        return convertDatabaseObjectToOrderDTO(order);
    }

    @Override
    public List<OrdersDTO> getAllOrders() {
        List<Order> orders = orderRepository.getAll();
        return getOrdersDTO(orders);
    }

    @Override
    public ObjectsDTOAndPagesEntity<OrdersDTO> getOrdersByPage(Integer page) {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_ORDERS_BY_PAGE);
        List<Order> orders = orderRepository.getPaginatedObjects(startPosition, COUNT_OF_ORDERS_BY_PAGE);
        int pages = getPages();
        List<OrdersDTO> ordersDTO = getOrdersDTO(orders);
        return new ObjectsDTOAndPagesEntity<>(pages, ordersDTO);
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.getById(id);
        if (order == null) {
            return null;
        }
        return convertDatabaseObjectToOrderDTO(order);
    }

    @Override
    public OrderDTO updateOrder(UpdateOrderDTO updateOrder) {
        Order order = orderRepository.getById(updateOrder.getId());
        if (order.getStatus() != updateOrder.getStatus()) {
            order.setStatus(updateOrder.getStatus());
        }
        return convertDatabaseObjectToOrderDTO(order);
    }

    @Override
    public ObjectsDTOAndPagesEntity<OrdersDTO> getUserOrdersByPage(Integer page) throws AnonymousUserException {
        int startPosition = getStartPositionByPageNumber(page, COUNT_OF_ORDERS_BY_PAGE);
        String authenticationName = getAuthenticationName();
        if (authenticationName.equals(ANONYMOUS_USER_NAME)) {
            throw new AnonymousUserException();
        }
        User user = userRepository.getUserByEmail(authenticationName);
        List<Order> orders = orderRepository.getPaginatedOrdersByCustomer(startPosition, COUNT_OF_ORDERS_BY_PAGE, user);
        List<OrdersDTO> ordersDTO = getOrdersDTO(orders);
        Long countOfOrders = orderRepository.getCountOfOrdersByUser(user);
        int pages = getCountOfPagesByCountOfObjects(countOfOrders, COUNT_OF_ORDERS_BY_PAGE);
        return new ObjectsDTOAndPagesEntity<>(pages, ordersDTO);
    }

    private List<OrdersDTO> getOrdersDTO(List<Order> orders) {
        return orders.stream()
                .map(OrderConversionUtil::convertDatabaseObjectToOrdersDTO)
                .collect(Collectors.toList());
    }

    private int getPages() {
        Long countOfOrders = orderRepository.getCountOfObjects();
        return getCountOfPagesByCountOfObjects(countOfOrders, COUNT_OF_ORDERS_BY_PAGE);
    }

    private String getAuthenticationName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private void checkAndSetUserDetails(Order order) throws CustomerDetailsException {
        String email = getAuthenticationName();
        User user = userRepository.getUserByEmail(email);
        String address = user.getUserDetails().getAddress();
        String telephone = user.getUserDetails().getTelephone();
        if (address == null || telephone == null) {
            throw new CustomerDetailsException();
        }
        if (address.equals("") || telephone.equals("")) {
            throw new CustomerDetailsException();
        }
        order.setCustomer(user);
    }

}
