from typing import List
from random import randint
from infraestructura.Banco import Banco
from entidades_de_negocio.Divisa import Divisa
from Tarjeta import Tarjeta
from TarjetaDebito import TarjetaDebito


class TarjetaCredito(Tarjeta):
    serialVersionUID = 1

    def _init_(self, noTarjeta: int, divisa: Divisa, creditoMaximo: float, interes: float):
        super()._init_(noTarjeta, divisa)
        self.CREDITOMAXIMO = creditoMaximo
        self.credito = 0
        self.INTERES = interes

    def _str_(self):
        return "Tipo de tarjeta: Crédito\n Número de tarjeta: {}\n Límite: {} {}\n Crédito: {} {}\n Tasa de interés: {}\n".format(
            self.noTarjeta, Banco.formatearNumero(self.CREDITOMAXIMO), self.divisa.name(), Banco.formatearNumero(self.credito), self.divisa.name(), self.INTERES
        )

    def transaccion(self, cantidad: float, t: TarjetaDebito) -> bool:
        if self.CREDITOMAXIMO - self.credito >= cantidad and t.getDivisa() == self.getDivisa():
            self.credito += cantidad
            t.setSaldo(t.getSaldo() + cantidad)
            return True
        else:
            return False

    def borrar(self) -> str:
        for c in Banco.getClientes():  # En caso de que queramos borrar, necesitamos quitar la tarjeta de todas los clientes que la tienen
            c.getTarjetasCredito().remove(self)
        return "La tarjeta de crédito #{} será borrada, ya que tiene demasiadas transacciones rechazadas".format(self.noTarjeta)

    def deshacerTransaccion(self, cantidad: float, t: Tarjeta) -> None:
        if self.CREDITOMAXIMO - self.credito >= cantidad and isinstance(t, TarjetaDebito):
            # Deshacer una transacción desde una tarjeta de débito
            self.credito += (
                int(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            )
            t.setSaldo(t.getSaldo() + cantidad)
        elif (
            self.CREDITOMAXIMO - self.credito >= cantidad
            and isinstance(t, TarjetaCredito)
            and t.getEspacio() >= cantidad
        ):
            # Deshacer una transacción desde otra tarjeta de crédito
            self.credito += (
                int(100 * cantidad * t.divisa.getValor() / self.divisa.getValor()) / 100
            )
            t.setCredito(cantidad)

    def puedeTransferir(self, monto: float) -> bool:
        return self.CREDITOMAXIMO - self.credito >= monto

    def tieneSaldo(self) -> bool:
        return self.credito != self.CREDITOMAXIMO

    def sacarDinero(self, monto: float) -> None:
        if self.credito + monto <= self.CREDITOMAXIMO:
            self.credito += monto

    def introducirDinero(self, monto: float) -> None:
        if self.credito - monto >= 0:
            self.credito -= monto

    @staticmethod
    def tarjetasDisponibles(puntaje: int, divisa: Divisa) -> List["TarjetaCredito"]:
        reqCredMax = {0: 100.0, 50: 500.0, 100: 1000.0, 150: 2000.0, 200: 3000.0}
        reqInteres = {0: 10.0, 50: 7.0, 100: 5.0, 150: 4.0, 200: 3.0}

        tarjetas = []
        noTarjeta = randint(100000000, 999999999)
        while Banco.numeroExistente(noTarjeta):
            noTarjeta = randint(100000000, 999999999)

        for i in reqCredMax.keys():
            if i > puntaje:
                break
            else:
                tarjetas.append(
                    TarjetaCredito(
                        noTarjeta,
                        divisa,
                        int(100 * reqCredMax[i] / divisa.getValor()) / 100,
                        reqInteres[i],
                    )
                )
        return tarjetas

    def getEspacio(self) -> float:
        return self.CREDITOMAXIMO - self.credito

    def setCredito(self, credito: float) -> None:
        if credito >= 0:
            self.credito = credito

    @staticmethod
    def anadirTarjetaCredito(tarjeta: "TarjetaCredito", cliente: Cliente, bono: int) -> None:
        cliente.getTarjetasCredito().append(tarjeta)
        cliente.setBonoActual(bono)