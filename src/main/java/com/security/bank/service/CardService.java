package com.security.bank.service;

import java.util.Optional;
import java.util.Random;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.bank.dto.CardDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.Card;
import com.security.bank.entity.CardType;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.CardRepository;

@Service
public class CardService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    public String blockCard(Long accountNumber, Long cardNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        Account account2 = account.get();
        Card card = cardRepository.findByCardNumber(cardNumber).get();
        if(card!=null && card.getCardNumber().equals(card.getCardNumber())) {
            account2.setCard(null);
            accountRepository.save(account2);
            cardRepository.delete(card);
            return "Card Blocked Successfully";
        } else {
            throw new RuntimeException("No Card Found with the given cardNumber: "+ cardNumber);
        }
    }

    public String applyCard(Long accountNumber, CardDto cardDto) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(!account.isPresent()) {
            throw new RuntimeException("account with given account number not found!");
        }
        Account account2 = account.get();
        if(account2.getCard() != null && account2.getCard().getCardNumber() != null) {
            throw new RuntimeException("Account with number: "+accountNumber+" already has a card.");
        } else {
            Card card = new Card();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.YEAR, 5);
            
            card.setCardNumber(Long.parseLong(generateCardNumber()));
            card.setCvv(generateCvv());
            card.setAllocationDate(new Date());
            card.setExpiryDate(calendar.getTime());
            card.setCardHolderName(account2.getUser().getName());
            card.setStatus("ACTIVE");
            card.setPin(cardDto.getPin());
            switch (cardDto.getCardType()) {
                case "DEBIT_CLASSIC" -> {
                    card.setDailyLimit(20000);
                    card.setCardType(CardType.DEBIT_CLASSIC);
                }
                case "CREDIT_PREMIUM" -> {
                    card.setDailyLimit(50000);
                    card.setCardType(CardType.CREDIT_PREMIUM);
                }
                case "CREDIT_MASTER" -> {
                    card.setDailyLimit(75000);
                    card.setCardType(CardType.CREDIT_MASTER);
                }
                default -> {
                    card.setDailyLimit(40000);
                    card.setCardType(CardType.DEBIT_GLOBAL);
                }
            }
            // if(cardDto.getCardType() == CardType.DEBIT_CLASSIC.toString()) {
            //     card.setDailyLimit(20000);
            //     card.setCardType(CardType.DEBIT_CLASSIC);
            // } else if(cardDto.getCardType() == CardType.CREDIT_PREMIUM.toString()) {
            //     card.setDailyLimit(50000);
            //     card.setCardType(CardType.CREDIT_PREMIUM);
            // } else if(cardDto.getCardType() == CardType.CREDIT_MASTER.toString()) {
            //     card.setDailyLimit(75000);
            //     card.setCardType(CardType.CREDIT_MASTER);
            // } else {
            //     card.setDailyLimit(40000);
            //     card.setCardType(CardType.DEBIT_GLOBAL);
            // }
            cardRepository.save(card);
            account2.setCard(card);
            accountRepository.save(account2);
            return "New Card Allocated to account wih Number: "+ accountNumber;
        }
    }

    public void updateCardPinAndLimit(Card card, Long cardNumber) {
        Optional<Card> card2 = cardRepository.findByCardNumber(cardNumber);
        if(!card2.isPresent()) {
            throw new RuntimeException("card with given number not found!");
        }
        Card card3 = card2.get();
        Double cardLimit = card.getDailyLimit();
        switch(card3.getCardType()){
            case DEBIT_GLOBAL -> {
                if(cardLimit != 0 && card.getDailyLimit()<= 50000) card3.setDailyLimit(card.getDailyLimit());
            }
            case DEBIT_CLASSIC -> {
                if(cardLimit != 0 && card.getDailyLimit()<= 40000) card3.setDailyLimit(card.getDailyLimit());
            }
            case CREDIT_MASTER -> {
                if(cardLimit != 0 && card.getDailyLimit()<= 100000) card3.setDailyLimit(card.getDailyLimit());
            }
            case CREDIT_PREMIUM -> {
                if(cardLimit != 0 && card.getDailyLimit()<= 75000)  card3.setDailyLimit(card.getDailyLimit());
            }
        }

        // if(card.getCardType() == CardType.DEBIT_GLOBAL && cardLimit !=0 && card.getDailyLimit()<= 50000) {
        //     card3.setDailyLimit(card.getDailyLimit());
        // } else if(card.getCardType() == CardType.DEBIT_CLASSIC && cardLimit !=0 && card.getDailyLimit()<= 40000) {
        //     card3.setDailyLimit(card.getDailyLimit());
        // } else if(card.getCardType() == CardType.CREDIT_MASTER && cardLimit !=0 && card.getDailyLimit()<= 100000) {
        //     card3.setDailyLimit(card.getDailyLimit());
        // } else if(card.getCardType() == CardType.CREDIT_PREMIUM && cardLimit !=0 && card.getDailyLimit()<= 75000) {
        //     card3.setDailyLimit(card.getDailyLimit());
        // }

        if(card.getPin()!=null) {
            card3.setPin(card.getPin());
        }
        cardRepository.save(card3);
    }

    public int generateCvv() {
        Random random = new Random();
        // Generating a random 3-digit number for CVV
        return random.nextInt(900) + 100;
    }

    public String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();

        // Generating a 16-digit card number
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }
}
