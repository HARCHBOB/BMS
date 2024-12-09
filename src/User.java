import java.sql.Date;
import java.sql.Timestamp;

public class User {
    private int userId;
    private String vardasPavarde;
    private Date gimimoData;
    private String pasoNumeris;
    private String gatve;
    private String namas;
    private String butas;
    private Timestamp sukurimoData;

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getVardasPavarde() { return vardasPavarde; }
    public void setVardasPavarde(String vardasPavarde) { this.vardasPavarde = vardasPavarde; }

    public Date getGimimoData() { return gimimoData; }
    public void setGimimoData(Date gimimoData) { this.gimimoData = gimimoData; }

    public String getPasoNumeris() { return pasoNumeris; }
    public void setPasoNumeris(String pasoNumeris) { this.pasoNumeris = pasoNumeris; }

    public String getGatve() { return gatve; }
    public void setGatve(String gatve) { this.gatve = gatve; }

    public String getNamas() { return namas; }
    public void setNamas(String namas) { this.namas = namas; }

    public String getButas() { return butas; }
    public void setButas(String butas) { this.butas = butas; }

    public Timestamp getSukurimoData() { return sukurimoData; }
    public void setSukurimoData(Timestamp sukurimoData) { this.sukurimoData = sukurimoData; }
}
