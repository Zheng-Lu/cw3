package external;

import java.util.ArrayList;
import java.util.Iterator;
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
        Iterator<Transaction> T = this.transactions.iterator();
        while (T.hasNext()){
            Transaction trans = T.next();
            if (trans.equals(t)){
                T.remove();
                return true;
            }
        }
        return false;
    }


}
