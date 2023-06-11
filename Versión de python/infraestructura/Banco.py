from typing import List
from enum import Enum
from Canal import Canal
from entidades_de_negocio.Divisa import Divisa
from tarjetas.Tarjeta import Tarjeta
from entidades_de_negocio.Transaccion import Transaccion

class Banco():
    serialVersionUID = 1
    clientes = []
    canales = []


    @staticmethod
    def ordenarCanalesPorImpuestos(canales: List[Canal]):
        canales.sort()
        return canales

    @staticmethod
    def getClientes(cls):
        return cls.clientes

    @staticmethod
    def setClientes(clientes) -> None:
        Banco.clientes = clientes

    @staticmethod
    def agregarCliente(cliente: Cliente) -> None:
        Banco.clientes.add(cliente)

    @staticmethod
    def getCanales(cls):
        return cls.canales

    @staticmethod
    def setCanales(canales) -> None:
        Banco.canales = canales

    @staticmethod
    def agregarCanal(canal: Canal) -> None:
        Banco.canales.add(canal)

    @staticmethod
    def calcularPuntaje(trans) -> int:
        puntaje = 0
        for t in trans:
            puntaje += 2 * t.getCantidad() * t.getDivisa().getValor()
        return puntaje

    @staticmethod
    def seleccionarDivisa(cliente: Cliente):
        retorno = []
        for d in Divisa.values():
            for t in cliente.getTarjetas():
                if t.getDivisa() == d:
                    retorno.add(d)
                    break
        return retorno

    @staticmethod
    def numeroExistente(num: int) -> bool:
        valor = False
        for t in Tarjeta.getTarjetas():
            if num == t.getNoTarjeta():
                valor = True
                break
        return valor

    @staticmethod
    def generarPeticion(transaccion: Transaccion, mensaje: str) -> None:
        transaccion.setRetornable(False)
        Transaccion(transaccion, mensaje)

    @staticmethod
    def formatearNumero(numero: float) -> str:
        esLocale = Locale("es", "CO")
        df = DecimalFormat.getInstance(esLocale)
        df.applyPattern("#,##0.00")
        return df.format(numero)