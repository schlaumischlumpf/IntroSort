import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class HeapSortUI extends JFrame {

    private JTextField inputField;
    private JLabel originalArrayLabel;
    private JLabel sortedArrayLabel;
    private JLabel timeLabel;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);

    public HeapSortUI() {
        setTitle("Heapsort Algorithmus Visualisierung");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Heapsort Algorithmus");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel subtitleLabel = new JLabel("Ein effizienter Sortieralgorithmus mit O(n log n) Komplexität");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(230, 240, 250));
        subtitleLabel.setBorder(new EmptyBorder(0, 30, 10, 30));

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(PRIMARY_COLOR);
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);
        headerPanel.add(titleContainer, BorderLayout.WEST);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel inputLabel = new JLabel("Zahlenreihe eingeben:");
        inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        inputLabel.setForeground(TEXT_COLOR);

        inputField = new JTextField("18, -27, 0, 14, 42, 33, -7");
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inputField.setPreferredSize(new Dimension(0, 35));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);

        // Center Content Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Original Array Panel
        JPanel originalPanel = createResultPanel(
            "Ursprüngliches Array",
            "originalArrayLabel"
        );
        originalArrayLabel = (JLabel) originalPanel.getClientProperty("label");

        // Sorted Array Panel
        JPanel sortedPanel = createResultPanel(
            "Sortiertes Array",
            "sortedArrayLabel"
        );
        sortedArrayLabel = (JLabel) sortedPanel.getClientProperty("label");

        // Time Panel
        JPanel timePanel = createResultPanel(
            "Verarbeitungszeit",
            "timeLabel"
        );
        timeLabel = (JLabel) timePanel.getClientProperty("label");
        timeLabel.setText("0 ms");

        centerPanel.add(originalPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(sortedPanel);
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(timePanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton sortButton = createStyledButton("Sortieren", PRIMARY_COLOR, SECONDARY_COLOR);
        JButton clearButton = createStyledButton("Zurücksetzen", new Color(155, 89, 182), new Color(188, 110, 214));

        sortButton.addActionListener((ActionEvent e) -> sortArray());
        clearButton.addActionListener((ActionEvent e) -> clearFields());

        buttonPanel.add(sortButton);
        buttonPanel.add(clearButton);

        // Assemble
        add(headerPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.PAGE_START);
        add(new JScrollPane(centerPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createResultPanel(String title, String key) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(PRIMARY_COLOR);

        JLabel contentLabel = new JLabel("—");
        contentLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        contentLabel.setForeground(TEXT_COLOR);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentLabel, BorderLayout.CENTER);
        panel.putClientProperty("label", contentLabel);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void sortArray() {
        try {
            String[] tokens = inputField.getText().split(",");
            int[] arr = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                arr[i] = Integer.parseInt(tokens[i].trim());
            }

            int[] arrCopy = arr.clone();
            originalArrayLabel.setText(Arrays.toString(arrCopy));

            // Call the sorting algorithm
            long startTime = System.currentTimeMillis();
            HeapSortAlgo.heapsort(arr);
            long endTime = System.currentTimeMillis();

            sortedArrayLabel.setText(Arrays.toString(arr));
            timeLabel.setText((endTime - startTime) + " ms");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe! Bitte nur Zahlen durch Kommata getrennt eingeben.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        inputField.setText("18, -27, 0, 14, 42, 33, -7");
        originalArrayLabel.setText("—");
        sortedArrayLabel.setText("—");
        timeLabel.setText("0 ms");
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new HeapSortUI().setVisible(true);
        });
    }
}
