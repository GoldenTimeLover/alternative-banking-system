package core.dtos;

import core.entities.Notification;

import java.util.List;

public class NotificationDTO {

    public List<Notification> notificationList;

    public NotificationDTO(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

}
