package es.uji.ei1027.clubesportiu.model;

public class AdministrationFilters {
    private boolean pending;
    private boolean active;
    private boolean rejected;
    private boolean suspended;
    private boolean finished;


    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "AdministrationFilters{" +
                "pending=" + pending +
                ", active=" + active +
                ", rejected=" + rejected +
                ", suspended=" + suspended +
                ", finished=" + finished +
                '}';
    }
}
