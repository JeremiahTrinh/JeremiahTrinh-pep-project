package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerUser(Account user){
        return accountDAO.insertAccount(user);
    }

    public Account loginUser(Account user){
        return accountDAO.getAccount(user);
    }

    public List<Integer> getUsers(){
        return accountDAO.getAllAccounts();
    }
}
