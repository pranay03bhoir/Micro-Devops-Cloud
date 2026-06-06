package com.pranay.easybuy.cart_order.services.servicesImpl;

import com.pranay.easybuy.cart_order.dto.AddCartItemRequest;
import com.pranay.easybuy.cart_order.dto.CartResponse;
import com.pranay.easybuy.cart_order.dto.UpdateCartItemRequest;
import com.pranay.easybuy.cart_order.services.CartService;

public class CartServiceImpl implements CartService {

    @Override
    public CartResponse getCart(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCart'");
    }

    @Override
    public CartResponse addItem(String userId, AddCartItemRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItem'");
    }

    @Override
    public CartResponse updateItem(String userId, String productId, UpdateCartItemRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateItem'");
    }

    @Override
    public CartResponse removeItem(String userId, String productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeItem'");
    }

    @Override
    public void clearCart(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearCart'");
    }

}
