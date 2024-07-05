public class StandardBus extends Bus {
    public StandardBus(int capacity, double ticketPrice, double refundRate) {
        super("Standard", capacity, ticketPrice, refundRate);
    }

    /**
     * Generates the seating layout for the standard bus, indicating occupied and unoccupied seats.
     *
     * @param seatStatus An array of boolean values representing the occupancy status of each seat (true if available, false if occupied).
     * @return A string representation of the seating layout.
     */
    @Override
    public String getSeatsLayout(boolean[] seatStatus) {

        StringBuilder layout = new StringBuilder();

        for (int i = 0; i < seatStatus.length; i++) {
            layout.append(seatStatus[i] ? "*" : "X");
            if ((i + 1) % 2 == 0 && (i + 1) % 4 != 0) {
                layout.append(" |");
            }
            if ((i + 1) < getCapacity() && ((i + 1) % 4 != 0)) {
                layout.append(" ");
            }
            if ((i + 1) % 4 == 0 && (i + 1) < getCapacity()) {
                layout.append("\n");
            }
        }
        return layout.toString();
    }
}