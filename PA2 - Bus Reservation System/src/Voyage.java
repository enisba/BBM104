
public class Voyage {
    private String voyageID;
    private String departure;
    private String arrival;
    private Bus bus;
    private boolean[] seatStatus; // Keeps the occupancy status of seats (true = empty, false = full)
    private double totalRefunds = 0.0;  // a variable to store the total refund amount
    private boolean isCancelled = false; // To track whether the flight has been canceled

    /**
     * Constructs a Voyage with specified parameters.
     *
     * @param voyageID  A unique identifier for the voyage.
     * @param departure The starting point of the voyage.
     * @param arrival   The destination of the voyage.
     * @param bus       The Bus object used for the voyage, determining the type and capacity of the voyage.
     */

    public Voyage(String voyageID, String departure, String arrival, Bus bus) {
        this.voyageID = voyageID;
        this.departure = departure;
        this.arrival = arrival;
        this.bus = bus;
        this.seatStatus = new boolean[bus.getCapacity()];
        // Initialize all seats as available.
        for (int i = 0; i < seatStatus.length; i++) {
            seatStatus[i] = true; // true = boş
        }
    }

    public String getVoyageID() {
        return voyageID;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public Bus getBus() {
        return bus;
    }

    public double getTotalRefunds() {
        return totalRefunds;
    }

    public void setTotalRefunds(double totalRefunds) {
        this.totalRefunds = totalRefunds;
    }

    public boolean[] getSeatStatus() {
        return seatStatus;
    }

    public double getTicketPrice() {
        return this.bus.getTicketPrice();
    }

    /**
     * Sells a ticket for a specified seat on the voyage, marking the seat as occupied.
     *
     * @param seatNumber The seat number (1-based index) to book.
     * @return true if the seat was available and successfully booked, false otherwise (if already booked).
     */
    public boolean sellTicketSeat(int seatNumber) {
        int index = seatNumber - 1;
        return index >= 0 && index < seatStatus.length && seatStatus[index]; // Invalid seat number or seat already occupied
    }

    /**
     * Cancels a ticket for a specified seat on the voyage, marking the seat as available again.
     *
     * @param seatNumber The seat number (1-based index) for which the ticket is to be cancelled.
     * @return true if the seat was previously booked and successfully cancelled, false otherwise (if already available).
     */
    public boolean cancelTicket(int seatNumber) {
        int index = seatNumber - 1;
        return index >= 0 && index < seatStatus.length && !seatStatus[index]; // invalid seat number or seat is already empty
    }

    /**
     * Calculates the total revenue generated from sold tickets, minus any refunds.
     *
     * @return The net revenue for the voyage.
     */
    public double calculateRevenue() {
        if (isCancelled) return totalRefunds;
        double totalRevenue = 0.0;
        for (int i = 0; i < seatStatus.length; i++) {
            if (!seatStatus[i]) {
                totalRevenue += bus.calculateTicketPrice(i + 1); // Calculate surcharge for premium seats
            }
        }
        return totalRevenue + totalRefunds;
    }

    /**
     * Provides a detailed description of the voyage including the voyage ID, route, seating layout, and revenue information.
     *
     * @return A string containing detailed information about the voyage.
     */
    public String getDetailedVoyageInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Voyage " + voyageID + "\n");
        sb.append(departure + "-" + arrival + "\n");
        sb.append(bus.getSeatsLayout(seatStatus));
        sb.append("\nRevenue: " + String.format("%.2f", calculateRevenue()));
        return sb.toString();
    }

    public void resetRevenue() {
        isCancelled = true;

    }

    public double getSeatTicketPrice(int seatNumber) {
        return bus.calculateTicketPrice(seatNumber);
    }

}
