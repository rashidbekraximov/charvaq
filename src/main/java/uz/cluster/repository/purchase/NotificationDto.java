package uz.cluster.repository.purchase;

public interface NotificationDto {

    int getId();

    int getDays();

    String getClient();

    String getLocation();

    double getDebtTotalValue();
}
