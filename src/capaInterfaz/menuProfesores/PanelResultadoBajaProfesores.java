
package capaInterfaz.menuProfesores;

import capaInterfaz.JButtonOp;
import capaInterfaz.JDialogOperacionFail;
import capaInterfaz.JDialogOperacionOK;
import capaInterfaz.JDialogOperacionWarning;
import capaInterfaz.listados.ListadoProfesor;
import capaInterfaz.menuPrincipal.FrameMenuPrincipal;
import capaInterfaz.menuPrincipal.PanelMenuPrincipal;
import capaLogicaNegocio.Profesor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/** Clase que gestiona el panel donde se muestra el resultado de una baja de profesores.
 *
 * @author Confiencial
 */
public class PanelResultadoBajaProfesores extends JPanel implements ActionListener{

    private static final int MENU_PRINCIPAL = 1;
    private static final int MENU_BAJA_PROFESORES = 42;
    private static final int BORRAR_PROFESOR = 421;

    private static final long serialVersionUID = 1L;

    private int x = 20,y = 140;
    private final int INCREMENTOX=140;
    private final int INCREMENTOY=30;

    private ArrayList<Integer> arrayCodProfesor = new ArrayList<Integer>();
    private ArrayList<JTextField> arrayNombre = new ArrayList<JTextField>();
    private ArrayList<JTextField> arrayApellidos = new ArrayList<JTextField>();
    private ArrayList<JTextField> arrayGrupoclase1 = new ArrayList<JTextField>();
    private ArrayList<JTextField> arrayGrupoclase2 = new ArrayList<JTextField>();

    private ArrayList<JRadioButton> arrayradio = new ArrayList<JRadioButton>();

    private List<ListadoProfesor> resultado_consulta = new ArrayList<ListadoProfesor>();
    private JButtonOp botonDelete, botonBack;
    private JLabel no_resul = new JLabel();

    private ButtonGroup grupoRadios = new ButtonGroup();


    /** Crea e inicializa un nuevo PanelResultadoBajaProfesores
     *
     * @param ancho n?? de pixels de anchura del panel.
     * @param alto n?? de pixels de altura del panel.
     */
    public PanelResultadoBajaProfesores(int ancho,int alto){
        this.setLayout(null);
        this.setBounds(0,0, ancho, alto);
        this.setPreferredSize(new Dimension(ancho,alto));

        this.cargarElementos();
    } // fin del constructor

    private void cargarElementos() {

        JLabel ruta = new JLabel("MENU PRINCIPAL>Profesores>Baja>Resultado de la b??squeda");
        ruta.setBounds(20,20,400,70);

        JLabel nombre = new JLabel("Nombre");
        nombre.setBounds(40,80,100,70);

        JLabel apellidos = new JLabel("Apellidos");
        apellidos.setBounds(180,80,100,70);

        JLabel grupo_clase1 = new JLabel("Grupo de clase 1");
        grupo_clase1.setBounds(320,80,100,70);

        JLabel grupo_clase2 = new JLabel("Grupo de clase 2");
        grupo_clase2.setBounds(460,80,100,70);


        ImageIcon img_home = new ImageIcon(getClass().getResource("/capaInterfaz/images/home_icon.jpg"));
        JButtonOp botonHome = new JButtonOp("",
                                            img_home,
                                            MENU_PRINCIPAL);
        botonHome.setBounds(670, 30, 80, 30);
        botonHome.addActionListener(this);

        botonDelete = new JButtonOp("Borrar",BORRAR_PROFESOR);
        botonDelete.setBounds(500, 500, 100, 30);
        botonDelete.addActionListener(this);

        botonBack = new JButtonOp("Cancelar",MENU_BAJA_PROFESORES);
        botonBack.setBounds(640, 500, 100, 30);
        botonBack.addActionListener(this);

        this.add(ruta);
        this.add(nombre);
        this.add(apellidos);
        this.add(grupo_clase1);
        this.add(grupo_clase2);
        this.add(botonHome);
        this.add(botonDelete);
        this.add(botonBack);
    } // fin del m??todo cargarElementos



    /** M??todo que captura los eventos del panel resultado baja profesores
     *  y ejecuta el c??digo correspondiente a cada evento.
     *
     * @param e evento capturado.
     */
    public void actionPerformed(ActionEvent e) {
        JButtonOp b = (JButtonOp) e.getSource();
        int ancho = FrameMenuPrincipal.ancho;
        int alto = FrameMenuPrincipal.alto;
        switch(b.getNumOperacion()){
            case MENU_PRINCIPAL:
                this.resetear();
                PanelMenuPrincipal pSubmenuPrincipal = new PanelMenuPrincipal(ancho,alto);
                FrameMenuPrincipal.getFramePrincipal().setContentPane(pSubmenuPrincipal);
                FrameMenuPrincipal.getFramePrincipal().setVisible(true);
                break;
            case MENU_BAJA_PROFESORES:
                this.resetear();
                PanelMenuBajaProfesores pSubmenuBajaProfesores = new PanelMenuBajaProfesores(ancho,alto);
                FrameMenuPrincipal.getFramePrincipal().setContentPane(pSubmenuBajaProfesores);
                FrameMenuPrincipal.getFramePrincipal().setVisible(true);
                break;
            case BORRAR_PROFESOR:

                int fila = this.filaSeleccionada();
                if (fila != -1) {

                    int cod = this.arrayCodProfesor.get(fila);
                    String nombre = this.arrayNombre.get(fila).getText();
                    String apellidos = this.arrayApellidos.get(fila).getText();
                    String grupo_clase1 = this.arrayGrupoclase1.get(fila).getText();
                    String grupo_clase2 = this.arrayGrupoclase2.get(fila).getText();

                    Profesor profesor = new Profesor (cod, nombre, apellidos,
                                                      grupo_clase1, grupo_clase2);
                    if (profesor.tieneTutorias()) {
                        JDialogOperacionWarning dialogWarning = new JDialogOperacionWarning(FrameMenuPrincipal.getFramePrincipal(),
                                                                                            "Revise que el profesor no est?? tutorizando",
                                                                                            "ning??n grupo de pr??cticas.",
                                                                                            true);
                        dialogWarning.setVisible(true);
                    } else {
                        JDialogOperacionWarning dialogWarning = new JDialogOperacionWarning(FrameMenuPrincipal.getFramePrincipal(),
                                                                                            "Se perder??n TODOS los datos del profesor",
                                                                                            "??Desea continuar?",
                                                                                            true);
                        dialogWarning.setVisible(true); 
                        if (dialogWarning.operacionOK){

                            try {
                                profesor.bajaProfesor();
                                this.BorrarFilaSeleccionada(fila);
                                JDialogOperacionOK ok = new JDialogOperacionOK(
                                                    FrameMenuPrincipal.getFramePrincipal(),
                                                    "El profesor ha sido dado de baja",
                                                    true);
                                ok.setVisible(true);
                                fila = -1;
                            } catch (RuntimeException e1) {
                                JDialogOperacionFail fail = new JDialogOperacionFail(
                                                    FrameMenuPrincipal.getFramePrincipal(),
                                                    e1.getMessage(),
                                                    true);
                                fail.setVisible(true);
                                fila = -1;
                                System.out.println(e1.getMessage());
                            }
                        }
                    }

                } else {
                    JDialogOperacionFail fail = new JDialogOperacionFail(
                                        FrameMenuPrincipal.getFramePrincipal(),
                                        "Seleccione una fila",true);
                    fail.setVisible(true);
                }
                break;
        }
    } // fin del m??todo actionPerformed


    /** M??todo que muestra al usuario los resultados de la consulta realizada.
     *  Si ocurre alg??n error, lanzar?? una excepci??n.
     *
     * @param resultado estructura que contiene el resultado de la consulta.
     */
    public void mostrarResultados(List<ListadoProfesor> resultado) {
        if (resultado.isEmpty()) {
            no_resul.setText("La consulta realizada no ha producido resultados");
            no_resul.setBounds(300,250,300,70);
            this.botonDelete.setEnabled(false);
            this.add(no_resul);
        } else {
            resultado_consulta = resultado;

            for (int i = 0; i < resultado.size(); i++){
		this.aniadir(i,resultado.get(i).getCodProfesor(),
                             resultado.get(i).getNombre(),
                             resultado.get(i).getApellidos(),
                             resultado.get(i).getGrupoClase1(),
                             resultado.get(i).getGrupoClase2());
                System.out.println();
             }
            this.modificarBoton(resultado.size());
            this.aumentarTamanio(resultado.size());
        }
    } // fin del m??todo mostrarResultados


    /** M??todo que a??ade una fila al resultado de la consulta.
     *
     * @param numreg n?? de fila
     * @param cod C??digo del profesor
     * @param nombre nombre del profesor
     * @param apellidos apellidos del profesor
     * @param grupo_clase1 Primer grupo de clase que imparte. Puede ser null.
     * @param grupo_clase2 Segundo grupo de clase que imparte. Puede ser null.
     */
    private void aniadir(int numreg, int cod, String nombre, String apellidos,
                         String grupo_clase1, String grupo_clase2) {


	JRadioButton radio = new JRadioButton();
	radio.setBounds(x, y+ (INCREMENTOY*numreg), 20, 20);
	grupoRadios.add(radio);
	arrayradio.add(radio);
	this.add(radio);

        arrayCodProfesor.add(numreg, cod);

        JTextField aux = new JTextField(nombre);
	aux.setBounds(x + 20, y+ (INCREMENTOY*numreg), 140, 20);
	aux.setEditable(false);
	arrayNombre.add(numreg, aux);
	this.add(aux);

	aux = new JTextField(apellidos);
	aux.setBounds(x + 20 + (INCREMENTOX), y+ (INCREMENTOY*numreg), 140, 20);
	aux.setEditable(false);
        arrayApellidos.add(numreg, aux);
	this.add(aux);

	if (this.noEstaVacio(grupo_clase1)) {
            aux = new JTextField(String.valueOf(grupo_clase1));
        } else {
            aux = new JTextField(String.valueOf(""));
        }
	aux.setBounds(x + 20 + (INCREMENTOX*2), y+ (INCREMENTOY*numreg), 140, 20);
	aux.setEditable(false);
        arrayGrupoclase1.add(numreg, aux);
	this.add(aux);

	if (this.noEstaVacio(grupo_clase2)) {
            aux = new JTextField(String.valueOf(grupo_clase2));
        } else {
            aux = new JTextField(String.valueOf(""));
        }
	aux.setBounds(x + 20 + (INCREMENTOX*3), y+ (INCREMENTOY*numreg), 140, 20);
	aux.setEditable(false);
        arrayGrupoclase2.add(numreg, aux);
	this.add(aux);

    } // fin del m??todo aniadir


    /** M??todo que vac??a las estructuras y reestablece el tama??o original
     *  del panel.
     *
     * Quita todos los campos de texto de la interfaz y vacia los arraylist.
     */
    public void resetear(){
        for (int i=0;i<arrayNombre.size();i++){
            this.remove(arrayNombre.get(i));
            this.remove(arrayCodProfesor.get(i));
            this.remove(arrayApellidos.get(i));
            this.remove(arrayGrupoclase1.get(i));
            this.remove(arrayGrupoclase2.get(i));
            this.remove(arrayradio.get(i));
        }
        arrayCodProfesor.clear();
        arrayNombre.clear();
        arrayApellidos.clear();
        arrayGrupoclase1.clear();
        arrayGrupoclase2.clear();
        arrayradio.clear();


        resultado_consulta.clear();

        this.setLayout(null);
        this.setBounds(0,0,800,600);
        this.setPreferredSize(new Dimension(800,600));

        no_resul.setText(" ");
        botonDelete.setBounds(500, 500, 100, 30);
        botonBack.setBounds(640, 500, 100, 30);
        botonDelete.setEnabled(true);
    } // fin del m??todo resetear



    /** M??todo que recoloca los botones "Borrar" y "Cancelar" en funci??n del
     *  n?? de filas que se hayan obtenido en la consulta.
     *
     * @param num_filas n?? de filas obtenidas como resultado de la consulta.
     */
    private void modificarBoton(int num_filas) {
        if ((y+(INCREMENTOY*num_filas) > 500)) {
            botonBack.setLocation(640, y+(INCREMENTOY*num_filas));
            botonDelete.setLocation(500, y+(INCREMENTOY*num_filas));
        }
    } // fin del m??todo modificarBoton


    /** M??todo que redefine el tama??o del panel en funci??n del
     *  n?? de filas que se hayan obtenido en la consulta.
     *
     * @param num_filas n?? de filas obtenidas como resultado de la consulta.
     */
    private void aumentarTamanio(int num_filas) {
	this.setPreferredSize(new Dimension(800,y + INCREMENTOY*num_filas + 70));
	this.updateUI();
    } // fin del m??todo aumentarTamanio


    public void BorrarFilaSeleccionada(int numreg){
        
        this.remove(arrayNombre.get(numreg));
        arrayNombre.remove(numreg);
        this.remove(arrayApellidos.get(numreg));
        arrayApellidos.remove(numreg);
        this.remove(arrayGrupoclase1.get(numreg));
        arrayGrupoclase1.remove(numreg);
        this.remove(arrayGrupoclase2.get(numreg));
        arrayGrupoclase2.remove(numreg);
        grupoRadios.remove(arrayradio.get(numreg));
        this.remove(arrayradio.get(numreg));
        arrayradio.remove(numreg);

        Point punto;

        for (int i = numreg; i<arrayNombre.size();i++){

                punto = arrayNombre.get(i).getLocation();
                punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
                arrayNombre.get(i).setLocation(punto);

                punto = arrayApellidos.get(i).getLocation();
                punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
                arrayApellidos.get(i).setLocation(punto);

                punto = arrayGrupoclase1.get(i).getLocation();
                punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
                arrayGrupoclase1.get(i).setLocation(punto);

                punto = arrayGrupoclase2.get(i).getLocation();
                punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
                arrayGrupoclase2.get(i).setLocation(punto);

                punto = arrayradio.get(i).getLocation();
                punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
                arrayradio.get(i).setLocation(punto);
        }
        punto = botonDelete.getLocation();
        punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
        botonDelete.setLocation(punto);

        punto = botonBack.getLocation();
        punto.setLocation(punto.getX(), punto.getY()-INCREMENTOY);
        botonBack.setLocation(punto);

        this.aumentarTamanio(arrayNombre.size());
    } // fin del m??todo BorrarFilaSeleccionada



    /** M??todo que devuelve el n?? de fila seleccionada por el usuario.
     *
     * @return Devuelve el n?? de fila seleccionada.
     *         Si el usuario no ha seleccionado ninguna fila, devolver?? -1.
     */
    public int filaSeleccionada(){
	int i=0;

	while (i<arrayradio.size()){
           if (arrayradio.get(i).isSelected()) {
                return i;
            }
            i++;
	}
	return -1;
    } // fin del m??todo filaSeleccionada




    /** M??todo que comprueba si una cadena no est?? vac??a. Se considerar?? vac??a
     * si su valor es null o "".
     *
     * @param cadena cadena de la que se quiere comprobar si no est?? vac??a.
     *
     * @return TRUE si la cadena no est?? vac??a.
     *         FALSE en caso contrario.
     */
    private boolean noEstaVacio(String cadena) {
	return cadena != null && !"".equals(cadena);
    } // fin del m??todo noEstaVacio

} // fin de la clase PanelResultadoBajaProfesores