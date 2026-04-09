package Components;

/**
 * Contrato que debe implementar cada opción del menú lateral.
 *
 * Ejemplo:
 *   new MenuItem() {
 *       public String getName() { return "Consultas"; }
 *       public void show()      { mainPanel.showConsultas(); }
 *   }
 */
public interface MenuItem {

    /** Texto que se muestra en el botón del menú. */
    String getName();

    /** Acción ejecutada cuando el usuario hace clic en este ítem. */
    void show();
}
