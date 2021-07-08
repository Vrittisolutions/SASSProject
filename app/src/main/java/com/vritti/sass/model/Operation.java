package com.vritti.sass.model;

import java.io.Serializable;

/**
 * Created by sharvari on 29-Nov-18.
 */

public class Operation implements Serializable {

    String OperationMasterId,Operation;

    public String getOperationMasterId() {
        return OperationMasterId;
    }

    public void setOperationMasterId(String operationMasterId) {
        OperationMasterId = operationMasterId;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }

    @Override
    public String toString() {
        return this.Operation;
    }
}
