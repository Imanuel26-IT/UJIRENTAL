package TesPromo;

import UJIRENTAL.controller.PromoServis;
import UJIRENTAL.model.PromoStatus;
import java.sql.SQLException;

public class TesPromo {
    public static void main(String[] args) throws SQLException {
        PromoServis servis = new PromoServis();

        for (PromoStatus status : servis.getAllPromoStatus()) {
            System.out.println(
                status.getIdPelanggan() + " | "
                + status.getNamaPelanggan() + " | "
                + status.getJumlahSewa() + "/"
                + status.getMinimalSewa() + " | "
                + status.getLabelStatus()
            );
        }
    }
}