package blackJack;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.BevelBorder;

public class BlackJackApp extends JFrame {
	private JPanel contentPane;
	String rules = "The dealer must hit his hand until 17 or better. Ties go to the dealer";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlackJackApp frame = new BlackJackApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BlackJackApp() {
		//Create Games Frame 
		createFrame();
		createTitleLabel();

		//Create button panel and buttons.
		JPanel buttonPanel = createButtonPanel();
		JButton btnHitMe = createHitMeButton(buttonPanel);
		JButton btnHold = createHoldButton(buttonPanel);
		JButton btnNewGame = new JButton("New Game");
		buttonPanel.add(btnNewGame);

		JPanel panelDisplay = createGamePanel();
		JPanel panelDealerCards = createDealerPanel(panelDisplay);
		JPanel panelPlayerCards = createPlayerPanel(panelDisplay);
		JPanel panelWhoWon = createWhoWonPanel(panelDisplay);

		JLabel lblWhoWon = new JLabel(rules);
		lblWhoWon.setForeground(Color.BLACK);
		lblWhoWon.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		panelWhoWon.add(lblWhoWon);

		//Make deck, shuffle, deal
		ArrayList<Card> deck = createDeck();
		Collections.shuffle(deck);

		//Dealing Hands
		Hand playerHand = new Hand(0, dealCard(deck), dealCard(deck));
		Hand dealerHand = new Hand(1, dealCard(deck), dealCard(deck));
		playerHand.setHandValue();
		dealerHand.setHandValue();

		//Update images for cards
		updateCardsPanel(panelPlayerCards, playerHand);
		updateDealerCardsPanel(panelDealerCards, dealerHand);

		//action listeners	
		int delay = 200; //milliseconds
		Timer gameChecker = new Timer(delay, null);
		gameChecker.setRepeats(true);
		gameChecker.start();

		//Hit Me Button
		btnHitMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerHand.hitHand(dealCard(deck));
				updateCardsPanel(panelPlayerCards, playerHand);				
				if (playerHand.getBust()) dealerPlay(deck, dealerHand, panelDealerCards);
			}
		});

		//Hold Hand button
		btnHold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerHand.setPlaying(false);
				dealerPlay(deck, dealerHand, panelDealerCards);
			}
		});
		
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!playerHand.getPlaying() && dealerHand.getPlaying()) {
					dealerPlay(deck, dealerHand, panelDealerCards);
				}
				else
					if (testGameOver(playerHand, dealerHand)) {
						updateCardsPanel(panelDealerCards, dealerHand);
						endOfGame(playerHand, dealerHand, lblWhoWon);
						gameChecker.setRepeats(false);
						btnHold.setEnabled(false);
						btnHitMe.setEnabled(false);
					}
			}
		};
		gameChecker.addActionListener(taskPerformer);

		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playerHand.getInHand().clear();
				dealerHand.getInHand().clear();
				deck.clear();
				
				//Re make deck
				int i;
				int idInitializer=0;
				for(Suit el: Suit.values())	{
					i = 1;
					for (Rank r: Rank.values())	{
						if (i==11) i = 10;
						deck.add(new Card(el, r, i, idInitializer));
						i++;
						idInitializer++;
					}
				}
				Collections.shuffle(deck);

				playerHand.getInHand().add(dealCard(deck));
				playerHand.getInHand().add(dealCard(deck));

				dealerHand.getInHand().add(dealCard(deck));
				dealerHand.getInHand().add(dealCard(deck));

				playerHand.setHandValue();
				dealerHand.setHandValue();

				playerHand.setPlaying(true);
				dealerHand.setPlaying(true);
				
				playerHand.setBust();
				dealerHand.setBust();

				updateCardsPanel(panelPlayerCards, playerHand);
				updateDealerCardsPanel(panelDealerCards, dealerHand);

				btnHold.setEnabled(true);
				btnHitMe.setEnabled(true);
				
				lblWhoWon.setText(rules);
				gameChecker.setRepeats(true);
				gameChecker.start();
			}
		});
	}

	private JPanel createWhoWonPanel(JPanel panelDisplay) {
		JPanel panelWhoWon = new JPanel();
		panelDisplay.add(panelWhoWon);
		return panelWhoWon;
	}

	public boolean testGameOver(Hand p, Hand d)	{
		if(p.getPlaying() || d.getPlaying()) return false;
		else return true;
	}

	public void endOfGame(Hand p, Hand d, JLabel j)	{
		j.setText(whoWon(p,d));
	}

	public String whoWon(Hand p, Hand d) {
		if (p.getBust()) {
			return "You Lost";
		}
		else 
			if (d.getBust()) {
				return "You won!";
			}
			else 
				if (p.getHandValue() > d.getHandValue()) {
					return "You won!";
				}
				else {
					return "You Lost";
				}
	}

	private JPanel createDealerPanel(JPanel panelDisplay) {
		JPanel panelDealer = new JPanel();
		panelDisplay.add(panelDealer);
		panelDealer.setLayout(new BorderLayout(0, 0));

		JLabel lblDealersHand = new JLabel("Dealer's Hand");
		lblDealersHand.setVerticalAlignment(SwingConstants.TOP);
		lblDealersHand.setHorizontalAlignment(SwingConstants.CENTER);
		panelDealer.add(lblDealersHand, BorderLayout.NORTH);

		JPanel panelDealerCards = new JPanel();
		panelDealer.add(panelDealerCards, BorderLayout.CENTER);
		return panelDealerCards;
	}

	private JPanel createPlayerPanel(JPanel panelDisplay) {
		JPanel panelPlayer = new JPanel();
		panelDisplay.add(panelPlayer);
		panelPlayer.setLayout(new BorderLayout(0, 0));

		JLabel lblPlayersHand = new JLabel("Player's Hand");
		lblPlayersHand.setHorizontalAlignment(SwingConstants.CENTER);
		panelPlayer.add(lblPlayersHand, BorderLayout.NORTH);

		JPanel panelPlayerCards = new JPanel();
		panelPlayer.add(panelPlayerCards, BorderLayout.CENTER);
		return panelPlayerCards;
	}

	private JPanel createGamePanel() {
		JPanel panelDisplay = new JPanel();
		panelDisplay.setBorder(new EmptyBorder(0, 0, 10, 0));
		contentPane.add(panelDisplay, BorderLayout.CENTER);
		panelDisplay.setLayout(new GridLayout(3, 1, 25, 0));
		return panelDisplay;
	}

	private JButton createHoldButton(JPanel buttonPanel) {
		JButton btnHold = new JButton("Hold");
		buttonPanel.add(btnHold);
		return btnHold;
	}

	private JButton createHitMeButton(JPanel buttonPanel) {
		JButton btnHitMe = new JButton("Hit Me");
		buttonPanel.add(btnHitMe);
		return btnHitMe;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		return buttonPanel;
	}

	private void createTitleLabel() {
		JLabel lblBlueDragonsBlackjack = new JLabel("Blue Dragons Blackjack\n\n");
		lblBlueDragonsBlackjack.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		lblBlueDragonsBlackjack.setForeground(Color.BLUE);
		lblBlueDragonsBlackjack.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		lblBlueDragonsBlackjack.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblBlueDragonsBlackjack, BorderLayout.NORTH);
	}

	private void updateCardsPanel(JPanel p, Hand h)	{
		p.removeAll();
		p.repaint();
		ArrayList<JLabel> cardLabels = new ArrayList<>();

		for (Card el : h.getInHand()) {
			JLabel cardLabel = new JLabel("");
			ImageIcon img = new ImageIcon(getClass().getResource("/blackJack/images/" + el.getId() + ".png"));
			Image imgTemp = img.getImage();
			Image newImg = imgTemp.getScaledInstance(125, 191, java.awt.Image.SCALE_SMOOTH);
			img = new ImageIcon(newImg);
			cardLabel.setIcon(img);
			cardLabels.add(cardLabel);
		}

		for (JLabel el : cardLabels) {
			p.add(el);
		}
		
		p.repaint();
		p.revalidate();
	}

	private void updateDealerCardsPanel(JPanel p, Hand h) {
		p.removeAll();
		p.repaint();
		ArrayList<JLabel> cardLabels = new ArrayList<>();

		for (Card el : h.getInHand()) {
			JLabel cardLabel = new JLabel("");
			ImageIcon img = null;
			if (el.equals(h.getInHand().get(0))) {
				img = new ImageIcon(getClass().getResource("/blackJack/images/52.png"));
			}
			else {
				img = new ImageIcon(getClass().getResource("/blackJack/images/" + el.getId() + ".png"));
			}
			Image imgTemp = img.getImage();
			Image newImg = imgTemp.getScaledInstance(125, 191, java.awt.Image.SCALE_SMOOTH);
			img = new ImageIcon(newImg);
			cardLabel.setIcon(img);
			cardLabels.add(cardLabel);
		}
		for (JLabel el : cardLabels) {
			p.add(el);
		}
		
		p.repaint();
		p.revalidate();
	}

	private void dealerPlay(ArrayList<Card> deck, Hand h, JPanel p) {
		while (h.getHandValue() < 17 && !deck.isEmpty()) {
			h.hitHand(dealCard(deck));
			updateCardsPanel(p, h);		
		}
		h.setPlaying(false);
	}

	private void createFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 20, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	private static Card dealCard(ArrayList<Card> d)	{
		Card tempCard = d.get(0);
		d.remove(0);
		return tempCard;
	}

	//Used for testing
	private static void printDeck(ArrayList<Card> deck) {
		System.out.println("Deck Printing");
		for(Card el : deck)
			System.out.println(el);
	}

	private static ArrayList<Card> createDeck() {
		ArrayList<Card> deck = new ArrayList<>(52);
		int i;
		int idInitializer = 0;

		for(Suit el: Suit.values()) {
			i = 1;
			for (Rank r: Rank.values())	{
				if (i==11) i = 10;
				deck.add(new Card(el, r, i, idInitializer));
				i++;
				idInitializer++;
			}
		}
		return deck;
	}
}

