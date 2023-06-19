from enum import Enum
from typing import List, Any

class Divisa(Enum):
    LIBRA_ESTERLINA = 1.25
    EURO = 1.10
    DOLAR = 1.0
    RUBLO_RUSO = 0.013
    YEN_JAPONES = 0.0074
    PESO_COLOMBIANO = 0.00022

    def get_valor(self) -> float:
        """
        Obtiene el valor de la divisa.

        Returns:
            float: Valor de la divisa.
        """
        return self.value

    @staticmethod
    def verificar_orden(divisas: List['Divisa'], divisa: 'Divisa', orden: str) -> bool:
        """
        Verifica el orden de las divisas.

        Args:
            divisas (List[Divisa]): Lista de divisas.
            divisa (Divisa): Divisa a verificar.
            orden (str): Orden ("origen" o "destino") a verificar.

        Returns:
            bool: True si el orden es correcto, False en caso contrario.
        """
        if orden.lower() == "origen":
            index = divisas.index(divisa)
            return divisas[index] == divisas[0]
        elif orden.lower() == "destino":
            index = divisas.index(divisa)
            return divisas[index] == divisas[0]
        return False

    @staticmethod
    def convertir_divisas(divisas: List['Divisa'], canal, monto: float) -> tuple[float | Any, ...]:
        """
        Convierte un monto de una divisa a otra utilizando un canal de conversión.

        Args:
            divisas (List[Divisa]): Lista de divisas.
            canal: Canal de conversión.
            monto (float): Monto a convertir.

        Returns:
            tuple[float | Any, ...]: Tupla con los montos convertidos y el impuesto.
        """
        divisa_origen = divisas[0]
        divisa_destino = divisas[1]

        montos = []
        monto_final = monto

        impuesto = monto * (canal.getImpuesto() / 100)
        monto_final -= impuesto

        monto_final = monto_final * divisa_origen.get_valor()
        if divisa_destino == Divisa.DOLAR:
            monto_final = round(monto_final, 2)
            montos.append(monto_final)
            montos.append(impuesto)
            return tuple(montos)

        monto_final = round((monto_final / divisa_destino.get_valor()), 2)
        montos.append(monto_final)
        montos.append(impuesto)

        return tuple(montos)

    @staticmethod
    def encontrarDivisa(nomDivisa: str):
        """
        Encuentra una divisa por su nombre.

        Args:
            nomDivisa (str): Nombre de la divisa.

        Returns:
            Divisa: La divisa encontrada o None si no se encuentra.
        """
        for d in Divisa:
            if d.name == nomDivisa:
                return d

    def getValor(self):
        """
        Obtiene el valor de la divisa.

        Returns:
            float: Valor de la divisa.
        """
        return self.value
