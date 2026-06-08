package com.gestao.observer;

import com.gestao.model.Venda;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class VendaEventPublisherTest {

    @Test
    void notificar_chamaTodosObservers() {
        VendaEventPublisher publisher = new VendaEventPublisher();

        AtomicBoolean aCalled = new AtomicBoolean(false);
        AtomicBoolean bCalled = new AtomicBoolean(false);

        VendaObserver a = venda -> aCalled.set(true);
        VendaObserver b = venda -> bCalled.set(true);

        publisher.registrar(a);
        publisher.registrar(b);

        Venda v = new Venda();
        v.setId(100L);
        v.setNomeProduto("X");
        v.setData(LocalDate.now());

        publisher.notificar(v);

        assertTrue(aCalled.get());
        assertTrue(bCalled.get());
    }

    @Test
    void notificar_continuaMesmoSeObserverLanca() {
        VendaEventPublisher publisher = new VendaEventPublisher();

        AtomicBoolean okCalled = new AtomicBoolean(false);

        VendaObserver bad = venda -> { throw new RuntimeException("boom"); };
        VendaObserver good = venda -> okCalled.set(true);

        publisher.registrar(bad);
        publisher.registrar(good);

        Venda v = new Venda();
        v.setId(200L);
        v.setNomeProduto("Y");
        v.setData(LocalDate.now());

        publisher.notificar(v);

        assertTrue(okCalled.get(), "observer bom deve ser chamado mesmo se outro falha");
    }

    @Test
    void remover_observer_naoRecebeNotificacao() {
        VendaEventPublisher publisher = new VendaEventPublisher();

        AtomicBoolean called = new AtomicBoolean(false);
        VendaObserver o = venda -> called.set(true);

        publisher.registrar(o);
        publisher.remover(o);

        Venda v = new Venda();
        v.setId(300L);
        v.setNomeProduto("Z");
        v.setData(LocalDate.now());

        publisher.notificar(v);

        assertFalse(called.get(), "observer removido não deve ser chamado");
    }
}
