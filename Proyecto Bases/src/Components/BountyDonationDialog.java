package Components;

import TablesObj.Donation;
import TablesObj.Pet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BountyDonationDialog extends JDialog {

    private static final Logger LOG = Logger.getLogger(BountyDonationDialog.class.getName());

    /** idStatus "Adopted" en la BD. */
    private static final int STATUS_ADOPTED = 3;

    // ── Contexto ───────────────────────────────────────────────────
    private final Pet pet;
    private final int bountyAmount;
    private final int bountyIdCurrency;
    private final int idAdopter;

    // ── Selección actual ───────────────────────────────────────────
    private int  selectedId   = -1;
    private char selectedType = ' ';  // 'A' = Asociación, 'C' = Casa Cuna

    // ── Componentes ────────────────────────────────────────────────
    private final JTable  tblRecipients = new JTable();
    private final JLabel  lblSelected   = new JLabel("Ningún destinatario seleccionado");
    private final JButton btnConfirm    = buildStyledButton("Confirmar donación", true);
    private final JButton btnCancel     = buildStyledButton("Cancelar",           false);

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────

    public BountyDonationDialog(Frame parent, Pet pet,
                                int bountyAmount, int bountyIdCurrency,
                                int idAdopter) {
        super(parent, "Donar recompensa de adopción", true);
        this.pet              = pet;
        this.bountyAmount     = bountyAmount;
        this.bountyIdCurrency = bountyIdCurrency;
        this.idAdopter        = idAdopter;

        setUndecorated(false);
        setSize(580, 520);
        setLocationRelativeTo(parent);
        setResizable(false);
        setContentPane(buildRoot());

        loadRecipientsAsync();
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSTRUCCIÓN DE UI
    // ─────────────────────────────────────────────────────────────

    private JPanel buildRoot() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Format.COLOR_BG);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildCenter(),  BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);
        return root;
    }

    // ── Header ────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Format.COLOR_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Donar recompensa");
        title.setFont(Format.FONT_TITLE);
        title.setForeground(Format.COLOR_TEXT_ON_PRIMARY);

        JLabel amount = new JLabel("Monto: " + bountyAmount);
        amount.setFont(Format.FONT_PRICE);
        amount.setForeground(new Color(255, 255, 180));

        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.add(title);
        texts.add(Box.createVerticalStrut(4));
        texts.add(amount);

        JLabel info = new JLabel(
            "<html><div style='text-align:right;color:rgba(255,255,255,0.8);'>" +
            "Mascota #" + pet.getId() + "<br>" +
            "El bounty se donará al destinatario<br>" +
            "seleccionado y la mascota pasará a <b>Adopted</b>." +
            "</div></html>");
        info.setFont(Format.FONT_BODY_SMALL);

        header.add(texts, BorderLayout.WEST);
        header.add(info,  BorderLayout.EAST);
        return header;
    }

    // ── Centro: tabla de destinatarios ────────────────────────────

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Format.COLOR_BG);
        center.setBorder(BorderFactory.createEmptyBorder(16, 20, 8, 20));

        JLabel lblTitle = new JLabel("Selecciona el destinatario de la donación");
        lblTitle.setFont(Format.FONT_SUBTITLE);
        lblTitle.setForeground(Format.COLOR_PRIMARY);
        lblTitle.setBorder(Format.borderSection());

        styleTable(tblRecipients);
        tblRecipients.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tblRecipients.getSelectedRow();
                if (row == -1) return;

                Object idVal   = tblRecipients.getValueAt(row, 0);
                Object nameVal = tblRecipients.getValueAt(row, 1);
                Object typeVal = tblRecipients.getValueAt(row, 2);

                selectedId   = parseId(idVal);
                selectedType = "Casa Cuna".equals(typeVal) ? 'C' : 'A';

                lblSelected.setText("->  " + typeVal + ": " + nameVal + "  (id " + selectedId + ")");
                lblSelected.setForeground(Format.COLOR_STATUS_AVAILABLE);
                btnConfirm.setEnabled(true);
            }
        });

        JScrollPane scroll = new JScrollPane(tblRecipients);
        scroll.setBorder(BorderFactory.createLineBorder(Format.COLOR_DIVIDER));
        scroll.getViewport().setBackground(Format.COLOR_BG);

        lblSelected.setFont(Format.FONT_BODY_SMALL);
        lblSelected.setForeground(Format.COLOR_TEXT_SECONDARY);
        lblSelected.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        center.add(lblTitle,    BorderLayout.NORTH);
        center.add(scroll,      BorderLayout.CENTER);
        center.add(lblSelected, BorderLayout.SOUTH);
        return center;
    }

    // ── Footer ────────────────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        footer.setBackground(Format.COLOR_BG);
        footer.setBorder(Format.borderDivider());

        btnConfirm.setEnabled(false);
        btnConfirm.addActionListener(e -> onConfirm());
        btnCancel .addActionListener(e -> dispose());

        footer.add(btnCancel);
        footer.add(btnConfirm);
        return footer;
    }

    // ─────────────────────────────────────────────────────────────
    //  CARGA ASÍNCRONA DE DESTINATARIOS
    // ─────────────────────────────────────────────────────────────

    private void loadRecipientsAsync() {
        new SwingWorker<ArrayList<ArrayList<Object>>, Void>() {
            @Override
            protected ArrayList<ArrayList<Object>> doInBackground() {
                return Donation.getRecipients();
            }
            @Override
            protected void done() {
                try { fillTable(get()); }
                catch (Exception ex) { LOG.log(Level.SEVERE, "Error cargando destinatarios", ex); }
            }
        }.execute();
    }

    private void fillTable(ArrayList<ArrayList<Object>> rows) {
        String[] cols = { "ID", "Nombre", "Tipo" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (ArrayList<Object> row : rows) model.addRow(row.toArray());
        SwingUtilities.invokeLater(() -> tblRecipients.setModel(model));
    }

    // ─────────────────────────────────────────────────────────────
    //  LÓGICA DE CONFIRMACIÓN
    // ─────────────────────────────────────────────────────────────

    private void onConfirm() {
        if (selectedId == -1) return;

        btnConfirm.setEnabled(false);
        btnConfirm.setText("Guardando…");

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                ArrayList<String> rs = Pet.getPopupItem(pet.getId());
                boolean res = Donation.insertDonationTransaction(
                        bountyAmount, 
                        selectedType == 'A' ? selectedId : 0,  // idAssociation
                        bountyIdCurrency,
                        selectedType == 'C' ? selectedId : 0,  // idCribHouse 
                        idAdopter
                );
                pet.changePetStatus(idAdopter, 5, pet.getId());
                return res;

                
            }
            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(BountyDonationDialog.this,
                            "¡Donación registrada y mascota marcada como Adoptada!",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(BountyDonationDialog.this,
                            "Ocurrió un error. Intenta de nuevo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        btnConfirm.setEnabled(true);
                        btnConfirm.setText("Confirmar donación");
                    }
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Error inesperado al confirmar", ex);
                    btnConfirm.setEnabled(true);
                    btnConfirm.setText("Confirmar donación");
                }
            }
        }.execute();
    }

    // ─────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────

    private static void styleTable(JTable table) {
        table.setFont(Format.FONT_BODY_SMALL);
        table.setForeground(Format.COLOR_TEXT_PRIMARY);
        table.setBackground(Format.COLOR_BG);
        table.setGridColor(Format.COLOR_DIVIDER);
        table.setRowHeight(28);
        table.setSelectionBackground(Format.COLOR_PRIMARY_LIGHT);
        table.setSelectionForeground(Format.COLOR_TEXT_PRIMARY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(Format.FONT_BODY_SMALL);
        table.getTableHeader().setBackground(Format.COLOR_BG_SURFACE);
        table.getTableHeader().setForeground(Format.COLOR_TEXT_SECONDARY);
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Format.COLOR_DIVIDER));
    }

    private static JButton buildStyledButton(String text, boolean primary) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Format.enableAntiAlias(g2);
                Color base  = primary ? Format.COLOR_PRIMARY     : new Color(200, 200, 210);
                Color hover = primary ? Format.COLOR_PRIMARY_DARK : new Color(170, 170, 180);
                g2.setColor(getModel().isRollover() ? hover : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Format.RADIUS_BTN, Format.RADIUS_BTN);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Format.FONT_BODY);
        btn.setForeground(primary ? Format.COLOR_TEXT_ON_PRIMARY : Format.COLOR_TEXT_PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(primary ? 185 : 110, 34));
        return btn;
    }

    private static String safeGet(ArrayList<String> list, int i) {
        return (list != null && i < list.size()) ? list.get(i) : null;
    }

    private static int parseId(Object val) {
        if (val == null)                           return -1;
        if (val instanceof Integer  i)             return i;
        if (val instanceof Long     l)             return l.intValue();
        if (val instanceof java.math.BigDecimal b) return b.intValue();
        try { return Integer.parseInt(val.toString().trim()); }
        catch (NumberFormatException e)            { return -1; }
    }
}