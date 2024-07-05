import java.util.HashMap;
import java.util.Map;

/**
 * Manages all operations related to voyages including initialization, ticket sales, refunds,
 * information display, and cancellation.
 */
public class VoyageManagementSystem {
    static Map<Integer, Voyage> voyages = new HashMap<>();

    /**
     * Processes commands related to voyage management and outputs the results to a file.
     *
     * @param command    The command string containing details on what action to perform.
     * @param outputFile The file path where output related to the command execution will be written.
     */
    static void processCommand(String command, String outputFile) {
        String[] parts = command.split("\\t");
        switch (parts[0]) {
            case "INIT_VOYAGE":
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                if ((parts[1].equals("Minibus") && parts.length == 7) ||
                        (parts[1].equals("Standard") && parts.length == 8) ||
                        (parts[1].equals("Premium") && parts.length == 9)) {
                    initVoyage(parts, outputFile);
                } else {
                    FileOutput.writeToFile(outputFile, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                }
                break;
            case "SELL_TICKET":
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                sellTicket(parts, outputFile);
                break;
            case "REFUND_TICKET":
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                refundTicket(parts, outputFile);
                break;
            case "PRINT_VOYAGE":
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                printVoyage(parts, outputFile);
                break;
            case "Z_REPORT":
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                generateZReport(parts, outputFile);
                break;
            case "CANCEL_VOYAGE":
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                cancelVoyage(parts, outputFile);
                break;
            default:
                FileOutput.writeToFile(outputFile, "COMMAND: " + String.join("\t", parts), true, true);
                FileOutput.writeToFile(outputFile, "ERROR: There is no command namely " + parts[0] + "!", true, true);
                break;
        }
    }

    /**
     * Initializes a voyage and stores it in the system. Details are read from command parts and written to the output file.
     *
     * @param parts      The parts of the command containing all necessary data to create a new voyage.
     * @param outputFile The file where the operation output will be written.
     */
    private static void initVoyage(String[] parts, String outputFile) {

        String busType = parts[1];
        int id = Integer.parseInt(parts[2]);
        String from = parts[3];
        String to = parts[4];
        int row = Integer.parseInt(parts[5]);
        double price = Double.parseDouble(parts[6]);

        String formattedPrice = String.format("%.2f", price);
        Bus bus = null;

        if (id <= 0) {
            FileOutput.writeToFile(outputFile, "ERROR: " + id + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        }
        if (voyages.containsKey(id)) {
            FileOutput.writeToFile(outputFile, "ERROR: There is already a voyage with ID of " + id + "!", true, true);
            return;
        }
        if (row <= 0) {
            FileOutput.writeToFile(outputFile, "ERROR: " + row + " is not a positive integer, number of seat rows of a voyage must be a positive integer!", true, true);
            return;
        }
        if (price <= 0) {
            FileOutput.writeToFile(outputFile, "ERROR: " + (int) price + " is not a positive number, price must be a positive number!", true, true);
            return;
        }
        switch (busType) {
            case "Standard":
                double standardRefund = Double.parseDouble(parts[7]);
                if (standardRefund < 0 || standardRefund > 100) {
                    FileOutput.writeToFile(outputFile, "ERROR: " + (int) standardRefund + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                    return;
                }
                bus = new StandardBus(4 * row, price, standardRefund);
                break;
            case "Premium":
                double premiumRefund = Double.parseDouble(parts[7]);
                double premiumFee = Double.parseDouble(parts[8]);
                if (premiumRefund < 0 || premiumRefund > 100) {
                    FileOutput.writeToFile(outputFile, "ERROR: " + (int) premiumRefund + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                    return;
                }
                if (premiumFee < 0) {
                    FileOutput.writeToFile(outputFile, "ERROR: " + (int) premiumFee + " is not a non-negative integer, premium fee must be a non-negative integer!", true, true);
                    return;
                }
                bus = new PremiumBus(3 * row, price, premiumFee, premiumRefund);
                break;
            case "Minibus":
                bus = new Minibus(2 * row, price);
                break;
        }

        voyages.put(id, new Voyage(String.valueOf(id), from, to, bus));

        switch (busType) {
            case "Standard":
                FileOutput.writeToFile(outputFile, "Voyage " + id + " was initialized as a standard (2+2) voyage from " + from + " to " + to + " with " + formattedPrice + " TL priced " + 4 * row + " regular seats. Note that refunds will be " + parts[7] + "% less than the paid amount.", true, true);
                break;
            case "Minibus":
                FileOutput.writeToFile(outputFile, "Voyage " + id + " was initialized as a minibus (2) voyage from " + from + " to " + to + " with " + formattedPrice + " TL priced " + 2 * row + " regular seats. Note that minibus tickets are not refundable.", true, true);
                break;
            case "Premium":
                double premiumFee = Double.parseDouble(parts[8]);
                FileOutput.writeToFile(outputFile, "Voyage " + id + " was initialized as a premium (1+2) voyage from " + from + " to " + to + " with " + formattedPrice + " TL priced " + 2 * row + " regular seats and " + String.format("%.2f", price + price * premiumFee / 100) + " TL priced " + row + " premium seats. Note that refunds will be " + parts[7] + "% less than the paid amount.", true, true);
                break;
            default:
                FileOutput.writeToFile(outputFile, "ERROR: Invalid bus type!", true, true);
                break;
        }
    }

    /**
     * Sells a ticket for a specified seat on a specified voyage. Details are written to the output file.
     *
     * @param parts      The parts of the command containing details for the ticket sale.
     * @param outputFile The file where the operation output will be written.
     */
    private static void sellTicket(String[] parts, String outputFile) {
        if (parts.length != 3) {
            FileOutput.writeToFile(outputFile, "ERROR: Erroneous usage of \"SELL_TICKET\" command!", true, true);
            return;
        }
        int voyageId = Integer.parseInt(parts[1]);
        double totalPrice = 0.0;
        String[] seatNumbers = parts[2].split("_");
        boolean allSuccess = true; // true indicates that the ticket has been successfully sold

        Voyage voyage = voyages.get(voyageId);

        if (!voyages.containsKey(voyageId)) {
            FileOutput.writeToFile(outputFile, "ERROR: There is no voyage with ID of " + voyageId + "!", true, true);
            return;
        }
        for (String seat : seatNumbers) {
            int seatNumber = Integer.parseInt(seat);
            if (seatNumber <= 0) {
                FileOutput.writeToFile(outputFile, "ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!", true, true);
                return;
            }
            if (seatNumber > voyage.getBus().getCapacity()) {
                FileOutput.writeToFile(outputFile, "ERROR: There is no such a seat!", true, true);
                return;
            }
            if (!voyage.sellTicketSeat(seatNumber)) {
                FileOutput.writeToFile(outputFile, "ERROR: One or more seats already sold!", true, true);
                allSuccess = false;
                break;
            } else {
                totalPrice += voyage.getSeatTicketPrice(seatNumber);
            }
        }

        if (allSuccess) {
            FileOutput.writeToFile(outputFile, "Seat " + String.join("-", seatNumbers) + " of the Voyage " + voyageId + " from "
                    + voyage.getDeparture() + " to " + voyage.getArrival() + " was successfully sold for " + String.format("%.2f", totalPrice) + " TL.", true, true);

            for (String seat : seatNumbers) {
                int seatNumber = Integer.parseInt(seat) - 1;
                voyage.getSeatStatus()[seatNumber] = false;
            }
        }
    }

    /**
     * Processes a ticket refund for a specified seat on a specified voyage. Details are written to the output file.
     *
     * @param parts      The parts of the command containing details for the ticket refund.
     * @param outputFile The file where the operation output will be written.
     */
    private static void refundTicket(String[] parts, String outputFile) {
        if (parts.length != 3) {
            FileOutput.writeToFile(outputFile, "ERROR: Erroneous usage of \"REFUND_TICKET\" command!", true, true);
            return;
        }
        int voyageId = Integer.parseInt(parts[1]);

        if (!voyages.containsKey(voyageId)) {
            FileOutput.writeToFile(outputFile, "ERROR: There is no voyage with ID of " + voyageId + "!", true, true);
            return;
        }

        String[] seatNumbers = parts[2].split("_");
        boolean allSuccess = true; // if true, successfully indicates that the ticket has been canceled

        Voyage voyage = voyages.get(voyageId);
        Bus bus = voyage.getBus();
        String busType = bus.busType;

        for (String seat : seatNumbers) {
            int seatNum = Integer.parseInt(seat);
            if (seatNum <= 0) {
                FileOutput.writeToFile(outputFile, "ERROR: " + seat + " is not a positive integer, seat number must be a positive integer!", true, true);
                return;
            }
            if (busType.equals("Premium") || busType.equals("Standard")) {
                if (!voyage.cancelTicket(seatNum)) {
                    allSuccess = false;
                    if (seatNum > voyage.getBus().getCapacity()) {
                        FileOutput.writeToFile(outputFile, "ERROR: There is no such a seat!", true, true);
                        return;
                    }
                    if (voyage.getSeatStatus()[seatNum - 1]) {
                        FileOutput.writeToFile(outputFile, "ERROR: One or more seats are already empty!", true, true);
                        return;
                    }
                }
            }
        }

        if (allSuccess) {
            switch (busType) {
                case "Standard":
                    double standardRefund = voyage.getBus().getRefundRate();

                    FileOutput.writeToFile(outputFile, "Seat " + String.join("-", seatNumbers) + " of the Voyage " + voyageId + " from " + voyage.getDeparture() + " to " + voyage.getArrival() + " was successfully refunded for " + String.format("%.2f", seatNumbers.length * (voyage.getTicketPrice() - standardRefund * voyage.getTicketPrice() / 100)) + " TL.", true, true);
                    break;
                case "Premium":
                    double totalRefund = 0.0;

                    for (String seat : seatNumbers) {
                        int seatNum = Integer.parseInt(seat);
                        double ticketPrice = voyage.getSeatTicketPrice(seatNum);
                        double refundAmount = ticketPrice * (1 - (voyage.getBus().getRefundRate() / 100));
                        totalRefund += refundAmount;
                    }
                    FileOutput.writeToFile(outputFile, "Seat " + String.join("-", seatNumbers) + " of the Voyage " + voyageId + " from " + voyage.getDeparture() + " to " + voyage.getArrival() + " was successfully refunded for " + String.format("%.2f", totalRefund) + " TL.", true, true);
                    break;
                case "Minibus":
                    FileOutput.writeToFile(outputFile, "ERROR: Minibus tickets are not refundable!", true, true);
                    return;
                default:
                    FileOutput.writeToFile(outputFile, "ERROR: Wrong means of transportation entered!", true, true);
                    return;
            }

            for (String seat : seatNumbers) {
                int seatNumber = Integer.parseInt(seat);
                voyage.getSeatStatus()[seatNumber - 1] = true;
                double refundAmount = bus.calculateTicketPrice(seatNumber) - (bus.calculateTicketPrice(seatNumber) * (1 - voyage.getBus().getRefundRate() / 100));
                voyage.setTotalRefunds(voyage.getTotalRefunds() + refundAmount);
            }
        }
    }

    /**
     * Prints detailed information about a specific voyage. Details are written to the output file.
     *
     * @param parts      The parts of the command specifying which voyage to print.
     * @param outputFile The file where the operation output will be written.
     */
    private static void printVoyage(String[] parts, String outputFile) {
        if (parts.length != 2) {
            FileOutput.writeToFile(outputFile, "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
            return;
        }
        int voyageId = Integer.parseInt(parts[1]);

        if (voyageId <= 0) {
            FileOutput.writeToFile(outputFile, "ERROR: " + parts[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        }
        if (voyages.containsKey(voyageId)) {
            FileOutput.writeToFile(outputFile, voyages.get(voyageId).getDetailedVoyageInfo(), true, true);
        } else {
            FileOutput.writeToFile(outputFile, "ERROR: There is no voyage with ID of " + voyageId + "!", true, true);
        }
    }

    /**
     * Generates a report summarizing all voyages, their revenues, and other details.
     * This Z report is written to the output file.
     *
     * @param parts      The parts of the command for generating the Z report.
     * @param outputFile The file where the operation output will be written.
     */
    static void generateZReport(String[] parts, String outputFile) {
        if (parts.length != 1) {
            FileOutput.writeToFile(outputFile, "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, true);
            return;
        }

        FileOutput.writeToFile(outputFile, "Z Report:\n----------------", true, true);

        if (voyages.isEmpty()) {
            FileOutput.writeToFile(outputFile, "No Voyages Available!", true, true);
            FileOutput.writeToFile(outputFile, "----------------", true, true);
        } else {
            for (Voyage voyage : voyages.values()) {
                FileOutput.writeToFile(outputFile, "Voyage " + voyage.getVoyageID(), true, true);
                FileOutput.writeToFile(outputFile, voyage.getDeparture() + "-" + voyage.getArrival(), true, true);
                FileOutput.writeToFile(outputFile, voyage.getBus().getSeatsLayout(voyage.getSeatStatus()), true, true);
                FileOutput.writeToFile(outputFile, "Revenue: " + String.format("%.2f", voyage.calculateRevenue()), true, true);
                FileOutput.writeToFile(outputFile, "----------------", true, true);
            }
        }
    }

    /**
     * Cancels a voyage and updates the system accordingly. Details of the cancellation are written to the output file.
     *
     * @param parts      The parts of the command specifying which voyage to cancel.
     * @param outputFile The file where the operation output will be written.
     */
    private static void cancelVoyage(String[] parts, String outputFile) {
        if (parts.length != 2) {
            FileOutput.writeToFile(outputFile, "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);
            return;
        }

        int voyageId = Integer.parseInt(parts[1]);

        if (voyageId <= 0) {
            FileOutput.writeToFile(outputFile, "ERROR: " + parts[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        }

        if (voyages.containsKey(voyageId)) {
            Voyage voyage = voyages.get(voyageId);

            FileOutput.writeToFile(outputFile, "Voyage " + voyageId + " was successfully cancelled!", true, true);
            FileOutput.writeToFile(outputFile, "Voyage details can be found below:", true, true);
            voyage.resetRevenue();
            FileOutput.writeToFile(outputFile, voyage.getDetailedVoyageInfo(), true, true);
            voyages.remove(voyageId);
        } else {
            FileOutput.writeToFile(outputFile, "ERROR: There is no voyage with ID of " + voyageId + "!", true, true);
        }
    }
}
