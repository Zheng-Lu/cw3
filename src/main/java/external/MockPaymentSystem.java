package external;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MockPaymentSystem implements PaymentSystem{

    public class Transaction{ // nested class to follow the transactions
        private String buyerEmail;
        private String sellerEmail;
        private double transactionAmount;

        public Transaction(String buyerEmail,String sellerEmail, double transactionAmount){
            this.buyerEmail = buyerEmail;
            this.sellerEmail = sellerEmail;
            this.transactionAmount = transactionAmount;
        }

        @Override
        public boolean equals(Object o){
            if (o == this){
                return true;
            }
            if(!(o instanceof Transaction)){
                return false;
            }
            Transaction transaction = (Transaction) o;
            return transactionAmount == transaction.transactionAmount &&
                    Objects.equals(buyerEmail,transaction.buyerEmail) &&
                    Objects.equals(sellerEmail,transaction.sellerEmail);
        }

        @Override
        public int hashCode(){
            return Objects.hash(buyerEmail,sellerEmail,transactionAmount);
        }
    }

    List<Transaction> transactions = new ArrayList<>();

    @Override
    public boolean processPayment(String buyerAccountEmail, String sellerAccountEmail,
                                  double transactionAmount){
        Transaction t = new Transaction(buyerAccountEmail,sellerAccountEmail,transactionAmount);
        transactions.add(t);
        return true;
    }

    @Override
    public boolean processRefund(String buyerAccountEmail, String sellerAccountEmail,
                                 double transactionAmount){
        Transaction t = new Transaction(buyerAccountEmail,sellerAccountEmail,transactionAmount);
        for(Transaction T: transactions){
            if(T.equals(t)){
                transactions.remove(T);
                return true;
            }
        }
        return false;
    }


}
