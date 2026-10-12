package com.ridesharing.dto;

import jakarta.validation.constraints.NotBlank;

public class CancelRideDto {
    @NotBlank(message = "cancelledBy must be specified (RIDER or DRIVER)")
    private String cancelledBy;

    @NotBlank(message = "cancellationReason is required")
    private String cancellationReason;

    private String notes;

    public CancelRideDto() {}

    public CancelRideDto(String cancelledBy, String cancellationReason, String notes) {
        this.cancelledBy = cancelledBy;
        this.cancellationReason = cancellationReason;
        this.notes = notes;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
