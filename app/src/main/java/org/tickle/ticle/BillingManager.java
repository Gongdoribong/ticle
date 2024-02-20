/*
package org.ligh.ticle;

import android.app.Activity;
import android.icu.text.AlphabeticIndex;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;

import java.io.IOException;
import java.util.List;

*
 * BillingManager 명세
 * - BillingManager : 생성자, billing client 생성 및 connection을 위한 함수 실행
 * - onPurchasesUpdated : 결제가 완료되거나 실패한 경우 처리
 * - handlePurchase : purchase token 설정(???), 구매 완료 후 로직 작성, 구매 상태 세팅(???)
 * - verifyValidSignature : Base64 인증(???)
 * - onAcknowledgePurchaseResponse : "Acknowledged" 출력
 * - consume_buy_Function : connection



public class BillingManager extends AppCompatActivity {

    private final Activity activity;
    private final BillingClient billingClient;

    public BillingManager(Activity activity) {
        this.activity = activity;
        this.billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
    }

    private void connectBillingClient() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                //인앱 결제 서비스와 연결이 끊어진 경우
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //인앱 결제 서비스와 연결된 경우
                    // 여기에 인앱 상품을 가져오거나 결제 기능을 추가하는 코드 작성
                }
            }
        });
    }


    PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
            //결제가 완료되거나 실패한 경우 처리하는 부분
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) {
                Toast.makeText(BillingManager.this, "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.DEVELOPER_ERROR) {
                Toast.makeText(BillingManager.this, "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.NETWORK_ERROR) {
                Toast.makeText(BillingManager.this, "NETWORK_ERROR", Toast.LENGTH_SHORT).show();
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                Toast.makeText(BillingManager.this, "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) {
                Toast.makeText(BillingManager.this, "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                Toast.makeText(BillingManager.this, "USER_CANCELED", Toast.LENGTH_SHORT).show();
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_NOT_OWNED) {
                Toast.makeText(BillingManager.this, "ITEM_NOT_OWNED", Toast.LENGTH_SHORT).show();
            } else {
                //기타 오류 처리
                Toast.makeText(BillingManager.this, ""+billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void handlePurchase(Purchase purchase) {

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //결제가 성공한 경우 처리하는 부분
                    //구매 완료 후의 로직 작성
                }
            }
        };
        billingClient.consumeAsync(consumeParams, listener);


        if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            //Verify
            //Verify the signature for the purchase
            if(!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                Toast.makeText(getApplicationContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                Toast.makeText(this, "Purchased", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already Purchased", Toast.LENGTH_SHORT).show();
            }
        } else if(purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Toast.makeText(this, "UNSPECIFIED_STATE", Toast.LENGTH_SHORT).show();
        } else if(purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            Toast.makeText(this, "PENDING", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyValidSignature(String originalJson, String signature) {
        try {
            // Base64 public key for verification
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAx/YWBiPHtdhGaCaORlYWy2Uc7lm/KnKSjzOTtnNsK3KTDdGXm0a7ITyxyZ4ZP0prRlJFBNRd/UnWyPI12prP7LXwtWtd9yCrGkvOj3NfddWzWfnr0hjQ03sYPNXf4s+WO5ssgoRBIkbZxWwLafjHrxbbiTKGeeDfEWrrZlLjsPsEeWJTXdMPiQPrsPuek9k0u3MriFNAtQ5i2/ZwL3bgctmkaNunix4HdIqyo96cfDs8OvVVZPOFzHFB9sKdaVWYKKhD2ZyAow34wIR9OROD4+8OUBQyM8xzcv0agM5ZNFrMRfMz4ravzsK2evKXohbNxIUhF4/K6zmAruzo9ayKywIDAQAB";
            return Verify.verifyPurchase(base64Key, originalJson, signature);
        } catch (IOException e) {
            return false;
        }
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
            Toast.makeText(BillingManager.this, "Acknowledged", Toast.LENGTH_SHORT).show();
        }
    };
    void consume_buy_Function() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    QueryProductDetailsParams queryProductDetailsParams =
                            QueryProductDetailsParams.newBuilder()
                                    .setProductL
                                            ImmutableList.of(
                                                    QueryProductDetailsParams.Product.newBuilder()
                                                            .setProductId("1won")
                                                            .setProductType(BillingClient.ProductType.INAPP)
                                                            .build()))
                                    .build();
                    billingClient.queryProductDetailsAsync(
                        queryProductDetailsParams,
                        new ProductDetailsResponseListener() {
                            @Override
                            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> productDetailsList) {
                                for(ProductDetails productDetails : productDetailsList) {
                                    ImmutableList productDetailsParamsList =
                                            ImmutableList.of(
                                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                                            .setProductDetails(productDetails)
                                                            .build()
                                            );
                                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                            .setProductDetailsParamsList(productDetailsParamsList)
                                            .build();

                                    billingClient.launchBillingFlow(BillingManager.this, billingFlowParams);

                                }
                            }
                        }
                    );
                }
            }
        });
    }


}
*/
