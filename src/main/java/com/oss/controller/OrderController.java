package com.oss.controller;

import com.oss.model.*;
import com.oss.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class OrderController {
    private final GhnService ghnService;
    @Autowired
    private HttpSession httpSession;

    @Autowired
    public OrderController(GhnService ghnService) {
        this.ghnService = ghnService;
    }
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

@Autowired
private ShippingAddressService shippingAddressService;
    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String Checkout(Model model, HttpSession session) {
        Map<String, String> provinces = ghnService.getProvinces();
        User user = (User) session.getAttribute("user");
        List<CartItem> cartItems = new ArrayList<>();
        if(user == null) {
           cartItems = cartService.getCart(session);
        }
        else{
            cartItems = cartService.getCartByUser(user);
        }



        model.addAttribute("provinces", provinces);
        model.addAttribute("cartItems", cartItems);
        return "customer/checkout";
    }
    @PostMapping("/checkout")
    public String processCheckoutForm(
            @RequestParam("firstName") String name,
            @RequestParam("phone") String phone,
            @RequestParam("province") String province,
            @RequestParam("district") String district,
            @RequestParam("address") String address,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("totalPrice") String totalPrice,
            @RequestParam("shippingFee") String shippingFee,
            HttpSession session,
            Model model) {

        // Assume user is retrieved from the session or authentication context


        // Create and save the order
        Order order = new Order();
        order.setTotalAmount(Double.parseDouble(totalPrice));
        order.setShippingFee(Double.parseDouble(shippingFee));
        order.setPaymentMethod(paymentMethod);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setStatus("SUBMITTED");
        User user = (User) session.getAttribute("user");
        System.out.println(user);
        List<CartItem> cartItems = cartService.getCart(session);

        if (user != null) {
            order.setUser(user);
            cartItems = cartService.getCartByUser(user);
        }


        orderService.saveOrder(order);
        ShippingAddress sa = new ShippingAddress();
        sa.setProvince(province);
        sa.setDistrict(district);
        sa.setAddress(address);
        sa.setName(name);
        sa.setPhone(phone);
        Set<Order> orders = new HashSet<>();
        orders.add(order);
        sa.setOrders(orders);
        // Retrieve cart items from session or other context

        // Create and save order items
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(productService.getProductById(cartItem.getProduct().getProductId()));
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            orderService.saveOrderItem(orderItem);
            cartService.removeFromCart(session, String.valueOf(cartItem.getProduct().getProductId()), user);
        }

        if ("VNPAY".equals(paymentMethod)) {
            // Redirect to VNpay controller
            return "redirect:/initiate_payment?orderId=" + order.getOrderId();
        } else {
            model.addAttribute("successMessage", "Order placed successfully!");
            return "common/thankyou";
        }

    }

    @GetMapping("/api/districts")
    @ResponseBody
    public Map<String, String> getDistricts(@RequestParam String provinceId) {
        return ghnService.getDistricts(provinceId);
    }

    @GetMapping("/api/wards")
    @ResponseBody
    public Map<String, String> getWards(@RequestParam String districtId) {
        return ghnService.getWards(districtId);
    }
    @GetMapping("/api/shipping-fee")
    @ResponseBody
    public double getShippingFee( @RequestParam String toWard, @RequestParam String toDistrictId, @RequestParam int  weight, @RequestParam int length, @RequestParam int width, @RequestParam int height) {
        return ghnService.calculateShippingFee(toWard, toDistrictId, weight, length, width, height);
    }

    @GetMapping("/order")
    public String getMyOrders(Model model){
        User user = (User) httpSession.getAttribute("user");
        List<Order> orders = orderService.findOrdersByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "customer/myorder";
    }

    @PostMapping("/orderdetail")
    public String loadOrderDetail(@RequestParam("orderid") Long orderId,
                                  Model model){
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "/customer/orderdetail";
    }
}
