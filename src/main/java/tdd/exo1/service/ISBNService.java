package tdd.exo1.service;

import tdd.exo1.exception.ISBNFormatException;

public class ISBNService {

    private final short isbnLength10 = 10;
    private final short isbnLength13 = 13;

    private final short moduloIsbn10 = 11;

    private final short moduloIsbn13 = 10;

    public boolean ValidatorISBN(String isbn) throws ISBNFormatException {
        int lengthRequired = isbn.length() > isbnLength10 ? isbnLength13 : isbnLength10;
        boolean isISBN13 = lengthRequired > isbnLength10;
        
        if (isbn.length() != lengthRequired)
            throw new ISBNFormatException("Bad ISBN length");

        String numericalIBN = isbn.substring(0, lengthRequired - 1);
        String lastCharIBN = isbn.substring(lengthRequired - 1);

        // Check of ISBN content (only numbers)
        if (!numericalIBN.matches("[0-9]+"))
            throw new ISBNFormatException("Bad ISBN characters format");

        // Check of ISBN last char (ISBN10 last char can be 'X')
        else if (!isISBN13 && !lastCharIBN.matches("[0-9]+") && !lastCharIBN.equals("X") // ISBN 10
        || isISBN13 && !lastCharIBN.matches("[0-9]+")) // ISBN 13
            throw new ISBNFormatException("Bad ISBN characters format");

        int total = 0;
        for (int i = 0; i < lengthRequired; i++) {
            int value = ((int) isbn.charAt(i)) * (10 - i);
            total += isISBN13 ? value * (i % 2 == 0 ? 3 : 1) : value * (10 - i);
        }
        return isISBN13 ? (total % moduloIsbn13 == 0) : (total % moduloIsbn10 == 0);
    }
}
