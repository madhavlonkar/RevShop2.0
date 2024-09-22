package com.revshop.repo;

import org.springframework.data.repository.CrudRepository;

import com.revshop.master.TransactionMaster;


public interface TransactionDAO extends CrudRepository<TransactionMaster, Integer> {
//    void saveTransaction(TransactionMaster transaction);
}
