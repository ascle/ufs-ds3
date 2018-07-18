package dominio;

import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
import static org.mockito.Mockito.*;
import persistencia.ReservaPassagensDao;

public class VooTest {

    private static Voo voo;
    private static ReservaPassagensDao mockReservaDao;
    private final static int CAPACIDADE_VOO = 10;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        mockReservaDao = mock(ReservaPassagensDao.class);
        voo = new Voo(1234, "Aracaju", "Fortaleza", Calendar.getInstance().getTime(), CAPACIDADE_VOO,
        		(ReservaPassagensDao) mockReservaDao);
    }

    
    @After
    public void tearDown() throws Exception {
         reset(mockReservaDao);
    }

    @Test
    public void testFazerReservaComSucesso() {
       Passageiro passageiro = new Passageiro("123123", "Fulano");
       List<Passagem> listaPassagem = new ArrayList<Passagem>();

       when(mockReservaDao.getPassagensPorVoo(voo)).thenReturn(listaPassagem);
       when(mockReservaDao.getPassagem(passageiro, voo)).thenReturn(null);
       when(mockReservaDao.salvarPassagem((Passagem) notNull())).thenReturn(true);
   
        assertTrue(voo.fazerReserva(passageiro));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFazerReservaSemPassageiro() {
    	List<Passagem> listaPassagem = new ArrayList<Passagem>();
    	
    	when(mockReservaDao.getPassagensPorVoo(voo)).thenReturn(listaPassagem);
    	when(mockReservaDao.getPassagem(null, voo)).thenReturn(null);
    	when(mockReservaDao.salvarPassagem((Passagem) notNull())).thenReturn(true);
    	
    	voo.fazerReserva(null);
    }
    
    @Test
    public void testFazerReservaSemVaga() {
    	Passageiro passageiro = new Passageiro("123123", "Fulano");
        List<Passagem> listaPassagem = new ArrayList<Passagem>();

        when(mockReservaDao.getPassagensPorVoo(voo)).thenReturn(listaPassagem);
        when(mockReservaDao.getPassagem(passageiro, voo)).thenReturn((Passagem) notNull());
    
         assertTrue(!voo.fazerReserva(passageiro));
    }
    
    @Test
    public void testFazerReservaSemSalvarPassagem() {
       Passageiro passageiro = new Passageiro("123123", "Fulano");
       List<Passagem> listaPassagem = new ArrayList<Passagem>();

       when(mockReservaDao.getPassagensPorVoo(voo)).thenReturn(listaPassagem);
       when(mockReservaDao.getPassagem(passageiro, voo)).thenReturn(null);
       when(mockReservaDao.salvarPassagem((Passagem) notNull())).thenReturn(false);
   
        assertTrue(!voo.fazerReserva(passageiro));
    }
    
    @Test
    public void testFazerReservaComUmaVaga() {
    	Passageiro passageiro = new Passageiro("123123", "Fulano");
        List<Passagem> listaPassagem = new ArrayList<Passagem>();
        
        // É legal isso?
        for(int i=0; i<(CAPACIDADE_VOO - 1); i++){
        	listaPassagem.add(new Passagem(i+"", voo, new Passageiro(i+"", i+"")));
        }

        when(mockReservaDao.getPassagensPorVoo(voo)).thenReturn(listaPassagem);
        when(mockReservaDao.getPassagem(passageiro, voo)).thenReturn(null);
        when(mockReservaDao.salvarPassagem((Passagem) notNull())).thenReturn(true);
    
         assertTrue(voo.fazerReserva(passageiro));
    }
    
    @Test
    public void testFazerReservaComMesmoPassageiro() {
    	Passageiro passageiro = new Passageiro("123123", "Fulano");
        List<Passagem> listaPassagem = new ArrayList<Passagem>();

        when(mockReservaDao.getPassagensPorVoo(voo)).thenReturn(listaPassagem);
        when(mockReservaDao.getPassagem(passageiro, voo)).thenReturn(new Passagem("", voo, passageiro));
        when(mockReservaDao.salvarPassagem((Passagem) notNull())).thenReturn(true);
    
         assertTrue(!voo.fazerReserva(passageiro));
    }

}