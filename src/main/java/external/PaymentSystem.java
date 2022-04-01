package external;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface PaymentSystem {


    boolean processPayment(String buyerAccountEmail, String sellerAccountEmail,
                           double transactionAmount);

    boolean processRefund(String buyerAccountEmail, String sellerAccountEmail,
                          double transactionAmount);

}
