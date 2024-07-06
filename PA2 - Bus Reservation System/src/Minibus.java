public class Minibus extends Bus {
    public Minibus(int capacity, double ticketPrice) {
        super("Minibus", capacity, ticketPrice);
    }

    /**
     * Generates the seating layout for the minibus, showing occupied and unoccupied seats.
     *
     * @param seatStatus An array of boolean values representing the occupancy status of each seat (true if available).
     * @return A string representation of the seating layout.
     */
    @Override
    public String getSeatsLayout(boolean[] seatStatus) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getCapacity(); i++) {
            sb.append(seatStatus[i] ? "*" : "X");
            if ((i + 1) < getCapacity() && (i + 1) % 2 != 0) {
                sb.append(" ");
            }
            if ((i + 1) % 2 == 0 && (i + 1) < getCapacity()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}