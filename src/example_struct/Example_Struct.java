package example_struct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author oracle
 */
public class Example_Struct {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        listarProductos();
        insireLinea(4001, 48, 20, 10, 1004, 0, 0);
      //  modificaLinea(5, "Alvaro Luna");
        //borraLinea(4001, 48);
    }

    public static void listarProductos() {
        try {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs;
            conn = SqlConnection.getConnection();
            String sql_SELECT = "SELECT * from empregadosbdor";
            stmt = conn.prepareStatement(sql_SELECT);
            rs = stmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                java.sql.Struct x = (java.sql.Struct) rs.getObject(2);
                Object[] campos = x.getAttributes();
                String nombre = (String) campos[0];
                java.math.BigDecimal edad = (java.math.BigDecimal) campos[1];
                int salario = rs.getInt("salario");
                System.out.println("id: " + id + " nombre: " + nombre + " edad: " + edad + " salario: " + salario);
            }
            SqlConnection.close(rs);
            SqlConnection.close(stmt);
            SqlConnection.close(conn);

        } catch (NullPointerException ex) {
            System.out.println("NULLL" + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }

    }

    public static void insireLinea(int ordnu, int linum, int cantidad, int descuento, int itemnum, int precio, int tasas) throws SQLException {
        /*
         probar a inserir unha linea de pedido para o pedido de numero de orden 4001 cos seguintes datos:
         linum: 48
         item : 2004
         cantidad: 20
         descuento: 10
         */
        Connection conn = null;
        PreparedStatement stmt = null;
        conn = SqlConnection.getConnection();
        String sql_INSERT = "INSERT INTO THE(SELECT P.pedido FROM pedido_tab P WHERE P.ordnum = ?) SELECT ?, REF(S), ?, ? FROM item_tab S WHERE S.itemnum = ?";
        stmt = conn.prepareStatement(sql_INSERT);
        stmt.setInt(1, ordnu);
        stmt.setInt(2, linum);
        stmt.setInt(3, cantidad);
        stmt.setInt(4, descuento);
        stmt.setInt(5, itemnum);
        stmt.executeUpdate();
        SqlConnection.close(stmt);
        SqlConnection.close(conn);
    }

    public static void modificaLinea(int clinumA, String nombre) throws SQLException {
        /*
         metodo modificaLinea  que modifique o nome dun cliente pasandolle 
         como minimo o numero do cliente. 
         probar a modificar o nome del cliente 5 para que pase a chamarse'Alvaro Luna'
         */
        String sql_UPDATE = " UPDATE cliente_tab SET clinomb='?' where clinum=?";
        Connection conn = null;
        PreparedStatement stmt = null;

        conn = SqlConnection.getConnection();
        stmt = conn.prepareStatement(sql_UPDATE);
        stmt.setString(1, nombre);
        stmt.setInt(2, clinumA);
        stmt.executeUpdate();
        SqlConnection.close(stmt);
        SqlConnection.close(conn);
    }

    public static void borraLinea(int ordnu, int linum) throws SQLException {
        /*
         c) metodo borraLinea que pasandolle como minimo
         o numero de orde dun pedido e un numero de linea,
         borre dita linea de dito pedido  
         probar a borrar a linea de pedido (linum) 
         48 do pedido cuxo ordnum e igual a 4001
         */
       // String sql_DELETE = " select cursor(select p.linum from Table(o.pedido) p where p.linum=48) from pedido_tab o where o.ordnum=4001";
        String sql_DELETE2 = "DELETE FROM THE(SELECT P.pedido FROM pedido_tab P WHERE P.ordnum = ?) WHERE linum = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        conn = SqlConnection.getConnection();
        stmt = conn.prepareStatement(sql_DELETE2);
        stmt.setInt(1, ordnu);
        stmt.setInt(2, linum);
        stmt.executeUpdate();
        SqlConnection.close(stmt);
        SqlConnection.close(conn);
        ;
    }
}
