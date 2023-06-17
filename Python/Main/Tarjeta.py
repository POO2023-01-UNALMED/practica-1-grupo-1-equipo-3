from abc import ABC, abstractmethod
from BorrarTarjeta import BorrarTarjeta
import pickle

class Tarjeta(ABC):
    tarjetas = []

    def _init_(self, noTarjeta, divisa):
        self.noTarjeta = noTarjeta
        self.divisa = divisa
        self.estado = "ACTIVA"
        self.transaccionesRechazadas = 0
        self.erroresActuales = 0
        Tarjeta.tarjetas.append(self)

    def getNoTarjeta(self):
        return self.noTarjeta

    def getDivisa(self):
        return self.divisa

    def anadirTransaccionRechazada(self):
        if self.transaccionesRechazadas >= 3:
            self.estado = "BLOQUEADA"
        self.transaccionesRechazadas += 1

    def getEstado(self):
        return self.estado

    def isActiva(self):
        return self.getEstado() == "ACTIVA"

    def tarjetaABorrar(self):
        return self.erroresActuales > BorrarTarjeta.erroresMax

    def setErroresActuales(self, errores):
        self.erroresActuales = errores

    def anadirError(self):
        self.erroresActuales += 1

    @staticmethod
    def getTarjetas():
        return Tarjeta.tarjetas

    @abstractmethod
    def transaccion(self, cantidad, t):
        pass

    @abstractmethod
    def deshacerTransaccion(self, cantidad, t):
        pass

    @abstractmethod
    def tieneSaldo(self):
        pass

    @abstractmethod
    def puedeTransferir(self, monto):
        pass

    @abstractmethod
    def sacarDinero(self, monto):
        pass

    @abstractmethod
    def introducirDinero(self, monto):
        pass

    @staticmethod
    def TarjetasBloqueadas(cliente):
        retorno = []
        for t in cliente.getTarjetasCredito():
            if not t.isActiva():
                retorno.append(t)
        for t in cliente.getTarjetasDebito():
            if not t.isActiva():
                retorno.append(t)
        return retorno

    @staticmethod
    def TarjetasNoBloqueadas(cliente):
        retorno = []
        for t in cliente.getTarjetasCredito():
            if t.isActiva():
                retorno.append(t)
        for t in cliente.getTarjetasDebito():
            if t.isActiva():
                retorno.append(t)
        return retorno

class TarjetaDebito(Tarjeta):
    def _init_(self, noTarjeta, divisa, saldo):
        super()._init_(noTarjeta, divisa)
        self.saldo = saldo

    def getSaldo(self):
        return self.saldo

    def setSaldo(self, saldo):
        self.saldo = saldo

    def _str_(self):
        from Banco import Banco
        return f"Tipo de tarjeta: Débito\nNúmero de tarjeta: {self.getNoTarjeta()}\nSaldo: {Banco.formatearNumero(self.saldo)} {self.divisa.name()}"

    def transaccion(self, cantidad, tarjeta):
        if self.saldo >= cantidad and tarjeta.getDivisa() == self.getDivisa():
            self.saldo -= cantidad
            tarjeta.setSaldo(tarjeta.getSaldo() + cantidad)
            return True
        else:
            return False

    def borrar(self):
        from Banco import Banco
        for c in Banco.getClientes():
            c.getTarjetasDebito().remove(self)
        return f"La tarjeta de crédito #{self.noTarjeta} será borrada, ya que tiene demasiadas transacciones rechazadas"

    def deshacerTransaccion(self, cantidad, t):
        if self.saldo >= cantidad and isinstance(t, TarjetaDebito):
            self.saldo -= round(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            t.setSaldo(t.getSaldo() + cantidad)
    
    