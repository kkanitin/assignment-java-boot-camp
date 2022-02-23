package com.example.skooldio.controller;

import com.example.skooldio.constant.TransactionStatus;
import com.example.skooldio.entity.Transaction;
import com.example.skooldio.model.request.TransactionModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.model.response.TransactionSummaryResponseModel;
import com.example.skooldio.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "TransactionController")
@RestController
@RequestMapping(path = "v1/transaction")
@Getter
@Setter
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    @ApiOperation(value = "Transaction user", response = Transaction.class)
    public ResponseModel<Transaction> create(@RequestBody TransactionModel model) {
        ResponseModel<Transaction> responseModel = new ResponseModel<>();
        try {
            Transaction transaction = service.create(model);
            responseModel.setData(transaction);

            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/confirmList")
    @ApiOperation(value = "update status to confirm by list of id", response = Transaction.class)
    public ResponseListModel<TransactionModel> updateStatusToConfirm(
            @RequestBody List<Long> listId
    ) {
        ResponseListModel<TransactionModel> responseModel = new ResponseListModel<>();
        try {
            List<TransactionModel> transactionModels = service.updateStatusList(listId, TransactionStatus.CONFIRM.name());
            responseModel.setMsg("Success");
            responseModel.setDatas(transactionModels);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/cancelled/{id}")
    @ApiOperation(value = "update status to shipping by id", response = Transaction.class)
    public ResponseModel<TransactionModel> updateStatusToCancelled(@PathVariable Long id) {
        ResponseModel<TransactionModel> responseModel = new ResponseModel<>();
        try {
            TransactionModel transactionModel = service.updateStatus(id, TransactionStatus.CANCELLED.name());
            responseModel.setMsg("Success");
            responseModel.setData(transactionModel);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/shipping/{id}")
    @ApiOperation(value = "update status to shipping by id", response = Transaction.class)
    public ResponseModel<TransactionModel> updateStatusToShipping(@PathVariable Long id) {
        ResponseModel<TransactionModel> responseModel = new ResponseModel<>();
        try {
            TransactionModel transactionModel = service.updateStatus(id, TransactionStatus.SHIPPING.name());
            responseModel.setMsg("Success");
            responseModel.setData(transactionModel);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/success/{id}")
    @ApiOperation(value = "update status to success by id", response = Transaction.class)
    public ResponseModel<TransactionModel> updateStatusToSucess(@PathVariable Long id) {
        ResponseModel<TransactionModel> responseModel = new ResponseModel<>();
        try {
            TransactionModel transactionModel = service.updateStatus(id, TransactionStatus.SUCCESS.name());
            responseModel.setMsg("Success");
            responseModel.setData(transactionModel);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/failed/{id}")
    @ApiOperation(value = "update status to success by id", response = Transaction.class)
    public ResponseModel<TransactionModel> updateStatusToFailed(@PathVariable Long id) {
        ResponseModel<TransactionModel> responseModel = new ResponseModel<>();
        try {
            TransactionModel transactionModel = service.updateStatus(id, TransactionStatus.FAILED.name());
            responseModel.setMsg("Success");
            responseModel.setData(transactionModel);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get Transaction by id", response = Transaction.class)
    public ResponseModel<Transaction> getById(@PathVariable Long id) {
        ResponseModel<Transaction> responseModel = new ResponseModel<>();
        try {
            Transaction transaction = service.getById(id);
            responseModel.setData(transaction);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

            if (null == transaction) {
                responseModel.setErrorMsg(String.format("Transaction id %d not found", id));
            }

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/listByGroupNumber/{groupNumber}")
    @ApiOperation(value = "summary by groupNumber", response = Transaction.class)
    public ResponseModel<TransactionSummaryResponseModel> listByGroupNumber(
            @PathVariable Integer groupNumber
    ) {
        ResponseModel<TransactionSummaryResponseModel> responseModel = new ResponseModel<>();
        try {
            TransactionSummaryResponseModel transaction = service.listByGroupNumber(groupNumber);
            responseModel.setData(transaction);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete Transaction by id", response = Transaction.class)
    public ResponseModel<Transaction> delete(@PathVariable Long id) {
        ResponseModel<Transaction> responseModel = new ResponseModel<>();
        try {
            service.deleteById(id);
            responseModel.setData(null);
            responseModel.setMsg(String.format("Transaction id %d deleted.", id));
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping
    @ApiOperation(value = "get Transaction as list", response = Transaction.class)
    public ResponseListModel<Transaction> listPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Transaction> responseListModel = new ResponseListModel<>();
        try {
            List<Transaction> transactions = service.listPaging(page, size, sort, dir);

            int countAll = service.countAll();
            int count = transactions.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(transactions);
            responseListModel.setAll(countAll);
            responseListModel.setCount(count);
            responseListModel.setNext(next);
            responseListModel.setMsg("Success");
            responseListModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @GetMapping("/listByUserId/{userId}")
    @ApiOperation(value = "get Transaction by userId as list", response = Transaction.class)
    public ResponseListModel<Transaction> listByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Transaction> responseListModel = new ResponseListModel<>();
        try {
            List<Transaction> transactions = service.listByUserId(userId, page, size, sort, dir);

            int countAll = service.countByUserId(userId);
            int count = transactions.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(transactions);
            responseListModel.setAll(countAll);
            responseListModel.setCount(count);
            responseListModel.setNext(next);
            responseListModel.setMsg("Success");
            responseListModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }
}
