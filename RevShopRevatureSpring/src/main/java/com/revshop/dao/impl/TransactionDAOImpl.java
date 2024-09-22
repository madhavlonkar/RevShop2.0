//package com.revshop.dao.impl;
//
//import com.revshop.dao.TransactionDAO;
//import com.revshop.master.TransactionMaster;
//import jakarta.persistence.EntityManager;
//import org.hibernate.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class TransactionDAOImpl implements TransactionDAO {
//
//    @Autowired
//    private EntityManager entityManager;
//
//    @Override
//    public void saveTransaction(TransactionMaster transaction) {
//        Session session = entityManager.unwrap(Session.class);
//        session.saveOrUpdate(transaction);
//    }
//}
