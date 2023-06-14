from enum import Enum
from typing import List
from typing import Tuple

class Divisa(Enum):
    LIBRA_ESTERLINA = 1.25
    EURO = 1.10
    DOLAR = 1.0
    RUBLO_RUSO = 0.013
    YEN_JAPONES = 0.0074
    PESO_COLOMBIANO = 0.00022

    def get_valor(self) -> float:
        return self.value

    @staticmethod
    def verificar_orden(divisas: List['Divisa'], divisa: 'Divisa', orden: str) -> bool:
        if orden.lower() == "origen":
            index = divisas.index(divisa)
            return divisas[index] == divisas[0]
        elif orden.lower() == "destino":
            index = divisas.index(divisa)
            return divisas[index] == divisas[0]
        return False

    @staticmethod
    def convertir_divisas(divisas: List['Divisa'], canal, monto: float) -> Tuple[float, float]:
        divisa_origen = divisas[0]
        divisa_destino = divisas[1]

        montos = []
        monto_final = monto

        impuesto = monto * (canal.get_impuesto() / 100)
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
    def encontrarDivisa(nomDivisa):
        for d in Divisa:
            if d.name == nomDivisa:
                return d