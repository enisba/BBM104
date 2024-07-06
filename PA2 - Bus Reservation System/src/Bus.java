public abstract class Bus {
    protected String busType;
    protected int capacity;
    protected double ticketPrice;
    private double refundRate;

    /**
     * Constructs a Bus with a refund rate. This using for Standard and Premium.
     *
     * @param busType The type of the bus (e.g., "Minibus", "Standard", "Premium").
     * @param capacity The maximum number of passengers that the bus can accommodate.
     * @param ticketPrice The base price for a ticket on this bus.
     * @param refundRate The percentage rate at which tickets are refunded (0-100). This using because minibus does not refundable
     */
    public Bus(String busType, int capacity, double ticketPrice,double refundRate) {
        this.busType = busType;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.refundRate = refundRate;
    }
    /**
     * Abstract base class for different types of buses in the booking system. Actually this using for only Minibus
     *
     * @param busType Type of the bus (e.g., Minibus, Standard, Premium).
     * @param capacity The maximum number of passengers the bus can accommodate.
     * @param ticketPrice The base price for a ticket on this bus.
     */
    public Bus(String busType, int capacity, double ticketPrice) {
        this.busType = busType;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getRefundRate() {
        return refundRate;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
    public double calculateTicketPrice(int seatNumber){
        return ticketPrice;
    }
    public abstract String getSeatsLayout(boolean[] seatStatus);

}
