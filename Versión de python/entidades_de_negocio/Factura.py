from typing import List
from typing import Tuple
from tarjetas.Tarjeta import Tarjeta
from tarjetas.TarjetaDebito import TarjetaDebito
from Transaccion import Transaccion
from infraestructura.Banco import Banco

class Factura:
    def _init_(self, cliente, total: float, transfeRestantes: int, tarjetaDestino):
        self.CLIENTE = cliente
        self.DIVISA = tarjetaDestino.getDivisa()
        self.TOTAL = total
        self.valorPagado = 0
        self.transfeRestantes = transfeRestantes
        self.TARJETADESTINO = tarjetaDestino
        self.facturaVencida = False
        self.facturaPagada = False
        cliente.agregarFactura(self)

    def getTransfeRestantes(self) -> int:
        return self.transfeRestantes

    def isFacturaVencida(self) -> bool:
        return self.facturaVencida

    def isFacturaPagada(self) -> bool:
        return not self.facturaPagada

    def getPendiente(self) -> float:
        return self.TOTAL - self.valorPagado

    def getTOTAL(self) -> float:
        return self.TOTAL

    def getDIVISA(self):
        return self.DIVISA

    def _str_(self) -> str:
        if self.facturaPagada and not self.facturaVencida:
            retorno = "Factura pagada ANTES de vencer\nTarjeta objetivo: " + str(self.TARJETADESTINO.getNoTarjeta()) + "\n"
        elif self.facturaPagada:
            retorno = "Factura pagada DESPUÉS de vencer\nTarjeta objetivo: " + str(self.TARJETADESTINO.getNoTarjeta()) + "\n"
        elif self.facturaVencida:
            retorno = "Factura vencida por pagar.1\nTarjeta objetivo: " + str(self.TARJETADESTINO.getNoTarjeta()) + " faltan " + Banco.formatearNumero((self.TOTAL - self.valorPagado)) + " " + self.DIVISA.name() + " por pagar" + "\n"
        else:
            retorno = "Factura no vencida por pagar.\nTarjeta objetivo: " + str(self.TARJETADESTINO.getNoTarjeta()) + " faltan " + Banco.formatearNumero(self.TOTAL - self.valorPagado) + " " + self.DIVISA.name() + " por pagar en " + str(self.transfeRestantes) + " transferencias" + "\n"
        return retorno

    def generarTransaccion(self, monto: float, tarjetaOrigen) -> 'Transaccion':
        validez = tarjetaOrigen.puedeTransferir(monto) and tarjetaOrigen.getDivisa() == self.TARJETADESTINO.getDivisa()
        return Transaccion(self.CLIENTE, tarjetaOrigen, self.TARJETADESTINO, monto, self, not validez)

    @staticmethod
    def modificarPuntaje(tarjetasBloqueadas: List[Tarjeta], tarjetasActivas: List[Tarjeta], cliente, puntaje: int) -> int:
        for f in cliente.getFactura():
            if f.facturaVencida:
                puntaje -= 50
            if f.facturaPagada and not f.facturaVencida:
                puntaje += 0.5 * f.TOTAL * f.DIVISA.getValor()
        for t in tarjetasActivas:
            if isinstance(t, TarjetaDebito):
                puntaje += int(0.1 * (t.getSaldo() * t.getDivisa().getValor()))
        puntaje -= 100 * len(tarjetasBloqueadas) / (len(tarjetasActivas) + len(tarjetasBloqueadas))
        puntaje += cliente.getBonoActual()
        return puntaje