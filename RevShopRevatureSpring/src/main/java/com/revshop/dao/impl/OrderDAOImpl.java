package com.revshop.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revshop.dao.OrderDAO;
import com.revshop.master.OrderDetails;
import com.revshop.master.OrderMaster;
import com.revshop.master.UserMaster;

import jakarta.persistence.EntityManager;

@Repository
public class OrderDAOImpl implements OrderDAO {

	@Autowired
	private EntityManager entityManager;

	@Override
	public OrderMaster saveOrder(OrderMaster order) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(order);
		return order;
	}

	@Override
	public void saveOrderDetails(OrderDetails orderDetails) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(orderDetails);
	}

	@Override
	public List<OrderMaster> findByUser(UserMaster user) {
		Session session = entityManager.unwrap(Session.class);
		String hql = "FROM OrderMaster WHERE user = :user";
		return session.createQuery(hql, OrderMaster.class).setParameter("user", user).getResultList();
	}

	@Override
	public boolean hasUserPurchasedProduct(UserMaster user, int productId) {
		Session session = entityManager.unwrap(Session.class);
		String hql = "SELECT COUNT(od) FROM OrderDetails od WHERE od.order.user = :user AND od.product.productId = :productId";
		Long count = session.createQuery(hql, Long.class).setParameter("user", user)
				.setParameter("productId", productId).uniqueResult();
		return count != null && count > 0;
	}

	// New method to find an order by ID
	@Override
	public OrderMaster findById(Long orderId) {
		Session session = entityManager.unwrap(Session.class);
		return session.get(OrderMaster.class, orderId);
	}

	@Override
	public List<OrderMaster> findOrdersBySeller(UserMaster seller) {
		Session session = entityManager.unwrap(Session.class);
		String hql = "SELECT om FROM OrderMaster om JOIN om.orderDetails od WHERE od.product.seller = :seller";
		return session.createQuery(hql, OrderMaster.class).setParameter("seller", seller).getResultList();
	}
}
