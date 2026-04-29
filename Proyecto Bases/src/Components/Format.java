package Components;
 
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
 
/**
 * Clase centralizada de formato visual para la aplicación "Quiero un Peludo".
 *
 * Contiene todos los colores, fuentes, bordes y dimensiones compartidos
 * entre los componentes de la UI. No se instancia; todo es estático.
 *
 * Uso:
 * <pre>
 *   label.setFont(Format.FONT_TITLE);
 *   panel.setBackground(Format.COLOR_BG);
 *   panel.setBorder(Format.borderCard());
 * </pre>
 */
public final class Format {
 
    private Format() { /* No instanciable */ }
 
    // ════════════════════════════════════════════════════════════════
    //  PALETA DE COLORES
    // ════════════════════════════════════════════════════════════════
 
    /** Fondo principal de la aplicación (blanco limpio). */
    public static final Color COLOR_BG              = Color.WHITE;
 
    /** Fondo secundario / superficies elevadas (gris muy suave). */
    public static final Color COLOR_BG_SURFACE      = new Color(250, 250, 252);
 
    /** Color primario de la marca: morado vibrante. */
    public static final Color COLOR_PRIMARY         = new Color(160,   0, 160);
 
    /** Variante más oscura del primario (hover / pressed). */
    public static final Color COLOR_PRIMARY_DARK    = new Color(110,   0, 110);
 
    /** Variante más clara del primario (fondo hover sutil). */
    public static final Color COLOR_PRIMARY_LIGHT   = new Color(248, 235, 255);
 
    /** Color de acento rojo para favoritos / alertas. */
    public static final Color COLOR_ACCENT_RED      = new Color(220,  50,  50);
 
    /** Color de acento verde para estado "Available". */
    public static final Color COLOR_STATUS_AVAILABLE    = new Color( 34, 139,  34);
    public static final Color COLOR_STATUS_AVAILABLE_BG = new Color(220, 255, 220);
 
    /** Color de acento amarillo/ámbar para "Pending Adoption". */
    public static final Color COLOR_STATUS_PENDING      = new Color(180, 120,   0);
    public static final Color COLOR_STATUS_PENDING_BG   = new Color(255, 243, 200);
 
    /** Color de acento azul para "Adopted". */
    public static final Color COLOR_STATUS_ADOPTED      = new Color( 30, 100, 200);
    public static final Color COLOR_STATUS_ADOPTED_BG   = new Color(220, 235, 255);
 
    /** Color gris para "In Treatment". */
    public static final Color COLOR_STATUS_TREATMENT    = new Color(100, 100, 100);
    public static final Color COLOR_STATUS_TREATMENT_BG = new Color(230, 230, 230);
 
    /** Texto principal (casi negro). */
    public static final Color COLOR_TEXT_PRIMARY    = new Color( 25,  25,  25);
 
    /** Texto secundario / metadatos (gris medio). */
    public static final Color COLOR_TEXT_SECONDARY  = new Color(100, 100, 110);
 
    /** Texto sobre fondo primario (blanco). */
    public static final Color COLOR_TEXT_ON_PRIMARY = Color.WHITE;
 
    /** Divisores y bordes sutiles. */
    public static final Color COLOR_DIVIDER         = new Color(220, 220, 228);
 
    /** Sombra para tarjetas (color semitransparente). */
    public static final Color COLOR_SHADOW          = new Color(0, 0, 0, 18);
 
    /** Overlay oscuro del popup (semitransparente). */
    public static final Color COLOR_OVERLAY         = new Color(0, 0, 0, 140);
 
 
    // ════════════════════════════════════════════════════════════════
    //  TIPOGRAFÍA
    // ════════════════════════════════════════════════════════════════
 
    /** Título grande (nombre mascota en popup, encabezados de sección). */
    public static final Font FONT_TITLE         = new Font("SansSerif", Font.BOLD,  20);
 
    /** Subtítulo / nombre de mascota en tarjeta. */
    public static final Font FONT_SUBTITLE      = new Font("SansSerif", Font.BOLD,  15);
 
    /** Cuerpo principal. */
    public static final Font FONT_BODY          = new Font("SansSerif", Font.PLAIN, 13);
 
    /** Cuerpo pequeño / metadatos. */
    public static final Font FONT_BODY_SMALL    = new Font("SansSerif", Font.PLAIN, 12);
 
    /** Etiquetas de badge (estado). */
    public static final Font FONT_BADGE         = new Font("SansSerif", Font.BOLD,  11);
 
    /** Precio destacado. */
    public static final Font FONT_PRICE         = new Font("SansSerif", Font.BOLD,  14);
 
    /** Botones de acción. */
    public static final Font FONT_BUTTON        = new Font("SansSerif", Font.BOLD,  8);
 
    /** Ícono / hamburguesa (tamaño grande). */
    public static final Font FONT_ICON          = new Font("SansSerif", Font.PLAIN, 16);
 
 
    // ════════════════════════════════════════════════════════════════
    //  DIMENSIONES
    // ════════════════════════════════════════════════════════════════
 
    /** Ancho de tarjeta de mascota. */
    public static final int CARD_WIDTH          = 260;
 
    /** Alto de la imagen en la tarjeta. */
    public static final int CARD_IMG_HEIGHT     = 200;
 
    /** Radio de esquinas redondeadas (tarjetas, botones, badges). */
    public static final int RADIUS_CARD         = 14;
    public static final int RADIUS_BTN          = 8;
    public static final int RADIUS_BADGE        = 20;
 
    /** Ancho del popup de detalle. */
    public static final int POPUP_WIDTH         = 540;
 
    /** Alto máximo del popup de detalle. */
    public static final int POPUP_HEIGHT        = 620;
 
    /** Padding interno estándar de tarjetas y paneles. */
    public static final int PAD_CARD            = 14;
 
    /** Espaciado entre filas de metadatos. */
    public static final int GAP_META            = 6;
 
 
    // ════════════════════════════════════════════════════════════════
    //  BORDES
    // ════════════════════════════════════════════════════════════════
 
    /** Borde vacío estándar para padding interno de tarjeta. */
    public static Border borderCardPadding() {
        return BorderFactory.createEmptyBorder(PAD_CARD, PAD_CARD, PAD_CARD, PAD_CARD);
    }
 
    /** Borde de línea sutil para divisores. */
    public static Border borderDivider() {
        return BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_DIVIDER);
    }
 
    /** Borde compuesto: línea + padding interno para secciones del popup. */
    public static Border borderSection() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_DIVIDER),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        );
    }
 
    /** Borde exterior de popup (línea sutil sobre fondo blanco). */
    public static Border borderPopup() {
        return BorderFactory.createLineBorder(COLOR_DIVIDER, 1, true);
    }
 
 
    // ════════════════════════════════════════════════════════════════
    //  HELPERS DE RENDERIZADO
    // ════════════════════════════════════════════════════════════════
 
    /**
     * Activa el antialiasing en un Graphics2D.
     * Llamar al inicio de cualquier paintComponent personalizado.
     */
    public static void enableAntiAlias(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);
    }
 
    /**
     * Devuelve el par (foreground, background) del color según el estado.
     * @param status  Texto del estado: "Available", "Pending Adoption",
     *                "Adopted", "In Treatment".
     * @return  Color[]{foreground, background}
     */
    public static Color[] statusColors(String status) {
        return switch (status == null ? "" : status.trim()) {
            case "Available"       -> new Color[]{COLOR_STATUS_AVAILABLE,  COLOR_STATUS_AVAILABLE_BG};
            case "Pending Adoption"-> new Color[]{COLOR_STATUS_PENDING,    COLOR_STATUS_PENDING_BG};
            case "Adopted"         -> new Color[]{COLOR_STATUS_ADOPTED,    COLOR_STATUS_ADOPTED_BG};
            default                -> new Color[]{COLOR_STATUS_TREATMENT,  COLOR_STATUS_TREATMENT_BG};
        };
    }
 
    /**
     * Crea un JLabel con estilo de "pill" de estado coloreada.
     * @param status  Texto del estado.
     */
    public static JLabel buildStatusBadge(String status) {
        Color[] colors = statusColors(status);
        JLabel badge = new JLabel(status, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                enableAntiAlias(g2);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), RADIUS_BADGE, RADIUS_BADGE);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(FONT_BADGE);
        badge.setForeground(colors[0]);
        badge.setBackground(colors[1]);
        badge.setOpaque(false);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return badge;
    }
 
    /**
     * Escala una imagen manteniendo relación de aspecto para cubrir
     * el área destino (tipo "cover").
     * @param img    Imagen original.
     * @param w      Ancho destino.
     * @param h      Alto destino.
     */
    public static Image scaleCover(Image img, int w, int h) {
        int iw = img.getWidth(null);
        int ih = img.getHeight(null);
        if (iw <= 0 || ih <= 0) return img;
        double scaleW = (double) w / iw;
        double scaleH = (double) h / ih;
        double scale  = Math.max(scaleW, scaleH);
        return img.getScaledInstance((int)(iw * scale), (int)(ih * scale),
                                     Image.SCALE_SMOOTH);
    }
 
    // ════════════════════════════════════════════════════════════════
    //  CARGA DE IMÁGENES DESDE URL
    // ════════════════════════════════════════════════════════════════
 
    /**
     * Descarga una imagen desde una URL HTTP/HTTPS de forma bloqueante.
     * Incluye User-Agent para evitar bloqueos de servidores como Wikimedia.
     * Llamar siempre desde un hilo de fondo (ej: SwingWorker.doInBackground).
     *
     * @param imageUrl  URL de la imagen (http o https).
     * @return          BufferedImage lista para usar, o {@code null} si falla.
     */
    public static BufferedImage loadImageFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(5_000);
            conn.setReadTimeout(10_000);
            conn.connect();
            return ImageIO.read(conn.getInputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
 
    /**
     * Versión asíncrona: descarga la imagen en un SwingWorker y llama
     * {@code onDone} en el EDT con el resultado (puede ser null si falla).
     * Uso recomendado desde constructores de componentes Swing.
     *
     * @param imageUrl  URL de la imagen.
     * @param onDone    Callback ejecutado en el EDT con la imagen resultante.
     */
    public static void loadImageAsync(String imageUrl,
                                      java.util.function.Consumer<BufferedImage> onDone) {
        new javax.swing.SwingWorker<BufferedImage, Void>() {
            @Override protected BufferedImage doInBackground() {
                return loadImageFromUrl(imageUrl);
            }
            @Override protected void done() {
                try { onDone.accept(get()); }
                catch (Exception ex) { onDone.accept(null); }
            }
        }.execute();
    }
 
}