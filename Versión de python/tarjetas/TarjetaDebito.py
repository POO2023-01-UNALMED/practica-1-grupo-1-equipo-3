from typing import List
from random import randint
from Tarjeta import Tarjeta
from entidades_de_negocio.Divisa import Divisa
from infraestructura.Banco import Banco
from TarjetaCredito import TarjetaCredito

class TarjetaDebito(Tarjeta):
    serialVersionUID = 1

    def _init_(self, noTarjeta: int, divisa: Divisa, saldo: float):
        super()._init_(noTarjeta, divisa)
        self.saldo = saldo

    def _str_(self):
        return "Tipo de tarjeta: Débito\nNúmero de tarjeta: {}\nSaldo: {} {}\n".format(
            self.noTarjeta, Banco.formatearNumero(self.saldo), self.divisa.name()
        )

    def transaccion(self, cantidad: float, tarjeta: "TarjetaDebito") -> bool:
        if self.saldo >= cantidad and tarjeta.getDivisa() == self.getDivisa():
            self.saldo -= cantidad
            tarjeta.setSaldo(tarjeta.getSaldo() + cantidad)
            return True
        else:
            return False

    def borrar(self) -> str:
        for c in Banco.getClientes():
            c.getTarjetasDebito().remove(self)
        return "La tarjeta de crédito #{} será borrada, ya que tiene demasiadas transacciones rechazadas".format(
            self.noTarjeta
        )

    def deshacerTransaccion(self, cantidad: float, t: Tarjeta) -> None:
        if self.saldo >= cantidad and isinstance(t, TarjetaDebito):
            self.saldo -= int(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            t.setSaldo(t.getSaldo() + cantidad)
        elif (
            self.saldo >= cantidad
            and isinstance(t, TarjetaCredito)
            and t.getEspacio() >= cantidad
        ):
            self.saldo -= int(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            t.setCredito(cantidad)

    def tieneSaldo(self) -> bool:
        return self.saldo != 0.0

    def puedeTransferir(self, monto: float) -> bool:
        return monto <= self.saldo

    def sacarDinero(self, monto: float) -> None:
        if self.saldo >= monto:
            self.saldo -= monto

    def introducirDinero(self, monto: float) -> None:
        self.saldo += monto