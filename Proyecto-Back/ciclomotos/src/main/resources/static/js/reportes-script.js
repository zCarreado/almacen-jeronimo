// Mostrar totales año, mes y día actuales al cargar la página
async function cargarTotalesActuales() {
  try {
    const [resAnio, resMes, resDia] = await Promise.all([
      fetch('/api/reportes/anio-actual'),
      fetch('/api/reportes/mes-actual'),
      fetch('/api/reportes/hoy'),
    ]);
    if (!resAnio.ok || !resMes.ok || !resDia.ok) throw new Error('Error al obtener reportes actuales');

    const reporteAnio = await resAnio.json();
    const reporteMes = await resMes.json();
    const reporteDia = await resDia.json();

    document.getElementById("ingresoVentasAnual").innerText = reporteAnio.totalGanancias.toFixed(2);
    document.getElementById("ingresoVentasMes").innerText = reporteMes.totalGanancias.toFixed(2);
    document.getElementById("ingresoVentasSemana").innerText = reporteDia.totalGanancias.toFixed(2);

  } catch (error) {
    console.error('Error al cargar totales actuales:', error);
  }
}

// Función para mostrar estadísticas en la tabla, usada para búsquedas específicas
function mostrarTablaReportes(data) {
  const tbody = document.getElementById('tablaReportesBody');
  tbody.innerHTML = ''; // limpiar tabla

  const stats = data.estadisticasPorCategoria;
  if (!stats) return;

  for (const categoria in stats) {
    if (Object.hasOwnProperty.call(stats, categoria)) {
      const stat = stats[categoria];
      const fila = document.createElement('tr');

      fila.innerHTML = `
        <td>${data.fechaInicio} a ${data.fechaFin}</td>
        <td>${categoria}</td>
        <td>${stat.cantidadVentas}</td>
        <td>$${stat.ganancias.toFixed(2)}</td>
        <td>${stat.porcentajeDelTotal.toFixed(2)}%</td>
      `;

      tbody.appendChild(fila);
    }
  }
}

// Búsquedas específicas que sólo actualizan la tabla, NO los cuadros de totales
async function buscarPorAnio(anio) {
  try {
    const res = await fetch(`/api/reportes/por-anio?anio=${anio}`);
    if (!res.ok) throw new Error('Error al obtener reporte por año');
    const reporte = await res.json();
    mostrarTablaReportes(reporte);
  } catch (error) {
    alert(error.message);
  }
}

async function buscarPorMes(anio, mes) {
  try {
    const res = await fetch(`/api/reportes/por-mes?anio=${anio}&mes=${mes}`);
    if (!res.ok) throw new Error('Error al obtener reporte por mes');
    const reporte = await res.json();
    mostrarTablaReportes(reporte);
  } catch (error) {
    alert(error.message);
  }
}

async function buscarPorDia(fecha) {
  try {
    const res = await fetch(`/api/reportes/por-dia?fecha=${fecha}`);
    if (!res.ok) throw new Error('Error al obtener reporte por día');
    const reporte = await res.json();
    mostrarTablaReportes(reporte);
  } catch (error) {
    alert(error.message);
  }
}

// Event listeners para los botones de búsqueda
document.getElementById('btnBuscarAnio').addEventListener('click', () => {
  const anio = document.getElementById('inputAnio').value;
  if (anio) buscarPorAnio(anio);
});

document.getElementById('btnBuscarMes').addEventListener('click', () => {
  const anio = document.getElementById('inputMesAnio').value;
  const mes = document.getElementById('inputMesMes').value;
  if (anio && mes) buscarPorMes(anio, mes);
});

document.getElementById('btnBuscarDia').addEventListener('click', () => {
  const fecha = document.getElementById('inputDia').value;
  if (fecha) buscarPorDia(fecha);
});

// Al cargar la página sólo cargar los totales actuales (sin tocar la tabla)
document.addEventListener('DOMContentLoaded', cargarTotalesActuales);
