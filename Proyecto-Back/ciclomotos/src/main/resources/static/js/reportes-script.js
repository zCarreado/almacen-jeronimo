let ventasGlobal = []; // Guardar todas las ventas para filtrar

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

// Mostrar estadísticas en la tabla de reportes (busquedas específicas)
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

// Búsquedas específicas (solo actualizan tabla, no totales)
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

// Cargar y mostrar lista de ventas con botón para factura
async function cargarVentas() {
  try {
    const res = await fetch('/api/ventas/obtenerVentas');
    if (!res.ok) throw new Error('Error al obtener lista de ventas');
    ventasGlobal = await res.json();

    mostrarVentas(ventasGlobal);

  } catch (error) {
    console.error('Error al cargar ventas:', error);
  }
}

function mostrarVentas(ventas) {
  const tbody = document.getElementById('tablaVentasBody');
  tbody.innerHTML = '';

  ventas.forEach(venta => {
    const fila = document.createElement('tr');

    const fechaFormateada = new Date(venta.fecha).toLocaleString('es-CO', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
    });

    fila.innerHTML = `
      <td>${venta.id}</td>
      <td>${fechaFormateada}</td>
      <td>${venta.cliente ? venta.cliente.nombre : 'N/A'}</td>
      <td>${venta.cliente ? venta.cliente.id : 'N/A'}</td>
      <td>$${venta.total.toFixed(2)}</td>
      <td>
        <button class="btn btn-sm btn-outline-primary btn-factura" data-id="${venta.id}">
          Generar Factura
        </button>
      </td>
    `;

    tbody.appendChild(fila);
  });

  // Eventos de botón para descargar factura
  document.querySelectorAll('.btn-factura').forEach(btn => {
    btn.addEventListener('click', () => {
      const idVenta = btn.getAttribute('data-id');
      descargarFacturaPdf(idVenta);
    });
  });
}

function descargarFacturaPdf(idVenta) {
  const url = `/api/ventas/factura-pdf/${idVenta}`;
  window.open(url, '_blank');
}

// Filtrado con búsqueda
function filtrarVentas(texto) {
  const textoLower = texto.toLowerCase();

  const filtradas = ventasGlobal.filter(venta => {
    const idStr = String(venta.id);
    const clienteNombre = (venta.cliente && venta.cliente.nombre) ? venta.cliente.nombre.toLowerCase() : '';
    const clienteDocumento = (venta.cliente && venta.cliente.id) ? String(venta.cliente.id).toLowerCase() : '';
    const fechaStr = new Date(venta.fecha).toLocaleDateString('es-CO');

    return (
      idStr.includes(textoLower) ||
      clienteNombre.includes(textoLower) ||
      clienteDocumento.includes(textoLower) ||
      fechaStr.includes(textoLower)
    );
  });

  mostrarVentas(filtradas);
}

// Event listeners para barra de búsqueda
document.getElementById('btnBuscarVenta').addEventListener('click', () => {
  const texto = document.getElementById('inputBuscarVenta').value.trim();
  filtrarVentas(texto);
});

document.getElementById('inputBuscarVenta').addEventListener('keyup', (e) => {
  if (e.key === 'Enter') {
    const texto = e.target.value.trim();
    filtrarVentas(texto);
  }
});

document.getElementById('btnLimpiarBusqueda').addEventListener('click', () => {
  document.getElementById('inputBuscarVenta').value = '';
  mostrarVentas(ventasGlobal);
});

// Event listeners para botones de búsqueda de reportes
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

// Al cargar la página, cargar totales actuales y lista de ventas
document.addEventListener('DOMContentLoaded', () => {
  cargarTotalesActuales();
  cargarVentas();
});
