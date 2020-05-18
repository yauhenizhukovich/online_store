package com.gmail.yauhenizhukovich.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.yauhenizhukovich.app.repository.ItemRepository;
import com.gmail.yauhenizhukovich.app.repository.OrderRepository;
import com.gmail.yauhenizhukovich.app.repository.UserRepository;
import com.gmail.yauhenizhukovich.app.repository.model.Item;
import com.gmail.yauhenizhukovich.app.repository.model.Order;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import com.gmail.yauhenizhukovich.app.service.impl.OrderServiceImpl;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.OrderDTO;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gmail.yauhenizhukovich.app.repository.model.StatusEnum.NEW;
import static com.gmail.yauhenizhukovich.app.service.constant.PageConstant.COUNT_OF_ORDERS_BY_PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.COUNT_OF_OBJECTS;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.PAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.START_POSITION;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ADDRESS;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_AMOUNT;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ID;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_NAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_PRICE;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_TELEPHONE;
import static com.gmail.yauhenizhukovich.app.service.util.PaginationUtil.getCountOfPagesByCountOfObjects;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        orderService = new OrderServiceImpl(orderRepository, itemRepository, userRepository);
    }

    @Test
    public void getOrdersByPage_callDatabase() {
        List<Order> returnedOrders = getOrders();
        when(orderRepository.getPaginatedObjects(START_POSITION, COUNT_OF_ORDERS_BY_PAGE))
                .thenReturn(returnedOrders);
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = orderService.getOrdersByPage(PAGE);
        List<OrdersDTO> actualOrders = ordersAndPages.getObjects();
        Assertions.assertThat(actualOrders).isNotNull();
        verify(orderRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_ORDERS_BY_PAGE);
    }

    @Test
    public void getOrdersByPage_returnOrders() {
        List<Order> returnedOrders = getOrders();
        when(orderRepository.getPaginatedObjects(START_POSITION, COUNT_OF_ORDERS_BY_PAGE))
                .thenReturn(returnedOrders);
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = orderService.getOrdersByPage(PAGE);
        List<OrdersDTO> actualOrders = ordersAndPages.getObjects();
        Assertions.assertThat(actualOrders).isNotNull();
        verify(orderRepository, times(1))
                .getPaginatedObjects(START_POSITION, COUNT_OF_ORDERS_BY_PAGE);
        OrdersDTO actualOrder = actualOrders.get(0);
        Order returnedOrder = returnedOrders.get(0);
        Assertions.assertThat(actualOrder.getAmount()).isEqualTo(returnedOrder.getAmount());
        Assertions.assertThat(actualOrder.getPrice()).isEqualTo(returnedOrder.getPrice());
        Assertions.assertThat(actualOrder.getStatus()).isEqualTo(returnedOrder.getStatus());
    }

    @Test
    public void getOrdersByPage_returnPages() {
        when(orderRepository.getCountOfObjects())
                .thenReturn(COUNT_OF_OBJECTS);
        ObjectsDTOAndPagesEntity<OrdersDTO> ordersAndPages = orderService.getOrdersByPage(PAGE);
        Assertions.assertThat(ordersAndPages).isNotNull();
        verify(orderRepository, times(1))
                .getCountOfObjects();
        Assertions.assertThat(ordersAndPages.getPages())
                .isEqualTo(getCountOfPagesByCountOfObjects(COUNT_OF_OBJECTS, COUNT_OF_ORDERS_BY_PAGE));
    }

    @Test
    public void getOrderById_returnOrder() {
        Order returnedOrder = getOrder();
        when(orderRepository.getById(VALID_ID)).thenReturn(returnedOrder);
        OrderDTO actualOrder = orderService.getOrderById(VALID_ID);
        Assertions.assertThat(actualOrder).isNotNull();
        verify(orderRepository, times(1)).getById(VALID_ID);
        Assertions.assertThat(actualOrder.getItemName()).isEqualTo(returnedOrder.getItem().getName());
        Assertions.assertThat(actualOrder.getAmount()).isEqualTo(returnedOrder.getAmount());
        Assertions.assertThat(actualOrder.getPrice()).isEqualTo(returnedOrder.getPrice());
    }

    @Test
    public void getOrderByNonexistentId_returnNull() {
        when(orderRepository.getById(VALID_ID)).thenReturn(null);
        OrderDTO actualOrder = orderService.getOrderById(VALID_ID);
        Assertions.assertThat(actualOrder).isNull();
        verify(orderRepository, times(1)).getById(VALID_ID);
    }

    @Test
    public void getAllOrders_callDatabase() {
        List<Order> returnedOrders = getOrders();
        when(orderRepository.getAll()).thenReturn(returnedOrders);
        List<OrdersDTO> actualOrders = orderService.getAllOrders();
        Assertions.assertThat(actualOrders).isNotNull();
        verify(orderRepository, times(1)).getAll();
    }

    @Test
    public void getAllOrders_returnOrders() {
        List<Order> returnedOrders = getOrders();
        when(orderRepository.getAll()).thenReturn(returnedOrders);
        List<OrdersDTO> actualOrders = orderService.getAllOrders();
        Assertions.assertThat(actualOrders).isNotNull();
        OrdersDTO actualOrder = actualOrders.get(0);
        Order returnedOrder = returnedOrders.get(0);
        Assertions.assertThat(actualOrder.getItemName()).isEqualTo(returnedOrder.getItem().getName());
        Assertions.assertThat(actualOrder.getAmount()).isEqualTo(returnedOrder.getAmount());
        Assertions.assertThat(actualOrder.getPrice()).isEqualTo(returnedOrder.getPrice());
    }

    private Order getOrder() {
        Order order = new Order();
        order.setStatus(NEW);
        order.setDate(LocalDate.now());
        order.setPrice(VALID_PRICE);
        order.setAmount(VALID_AMOUNT);
        User customer = new User();
        UserDetails userDetails = new UserDetails();
        userDetails.setTelephone(VALID_TELEPHONE);
        userDetails.setAddress(VALID_ADDRESS);
        customer.setUserDetails(userDetails);
        order.setCustomer(customer);
        Item item = new Item();
        item.setName(VALID_NAME);
        order.setItem(item);
        return order;
    }

    private List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(getOrder());
        return orders;
    }

}
