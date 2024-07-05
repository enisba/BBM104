public class PremiumBus extends Bus {
    private double premiumFee;

    /**
     * Constructs a Premium Bus with specific parameters for premium fee and refund rate.
     *
     * @param premiumFee The additional fee percentage for premium seats.
     */

    public PremiumBus(int capacity, double ticketPrice, double premiumFee, double refundRate) {
        super("Premium", capacity, ticketPrice, refundRate);
        this.premiumFee = premiumFee;
    }

    @Override
    public double calculateTicketPrice(int seatNumber) {
        if ((seatNumber) % 3 == 1) {
            return getTicketPrice() + getTicketPrice() * getPremiumFee() / 100;
        } else {
            return getTicketPrice();
        }
    }

    public double getPremiumFee() {
        return premiumFee;
    }

    /**
     * Calculates the ticket price for a given seat, considering if it's a premium seat.
     *
     * @param seatStatus The seat number for which the ticket price is calculated.
     * @return The ticket price, possibly increased by the premium fee for premium seats.
     */
    @Override
    public String getSeatsLayout(boolean[] seatStatus) {
        StringBuilder layout = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            layout.append(seatStatus[i] ? "*" : "X");
            if ((i + 1) % 3 == 1) {
                layout.append(" |");
            }
            if ((i + 1) < getCapacity() && ((i + 1) % 3 != 0)) {
                layout.append(" ");
            }
            if ((i + 1) % 3 == 0 && (i + 1) < getCapacity()) {
                layout.append("\n");
            }
        }
        return layout.toString();
    }

}
