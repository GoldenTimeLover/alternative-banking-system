package core.dtos;

import core.entities.Loan;

import java.util.List;

public class AdminSnapshot {

    public List<CustomerSnapshot> customerSnapshotList;
    public List<AdminLoanDTO> loanList;
    public String adminName = "";
    public int currentYaz = 1;
    public boolean isRewind = false;

    public AdminSnapshot(List<CustomerSnapshot> customerSnapshotList, String adminName, int currentYaz,List<AdminLoanDTO> loanList,boolean isRewind) {
        this.customerSnapshotList = customerSnapshotList;
        this.adminName = adminName;
        this.currentYaz = currentYaz;
        this.isRewind = isRewind;
        this.loanList = loanList;
    }
}
