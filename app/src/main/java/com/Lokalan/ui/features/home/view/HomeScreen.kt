package com.Lokalan.ui.features.home


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.Lokalan.R
import com.Lokalan.data.model.Budaya
import com.Lokalan.ui.features.home.viewmodel.HomeViewModel
import com.Lokalan.ui.navigation.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBudayaClick: (Int) -> Unit,
    onKategoriClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Mengelompokkan list budaya berdasarkan nama kategori secara otomatis
    val groupedBudaya = uiState.budayaList.groupBy { it.kategori?.namaKategori ?: "Lainnya" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.explore_culture),
                showProfileButton = true,
                onProfileClick = onProfileClick,
                showSearchButton = true,
                onSearchClick = { viewModel.toggleSearch() }
            )
        }
    ) { innerPadding -> // Perbaikan: Menggunakan nama unik untuk menghindari error padding
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.budayaList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_budaya_found),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Baris ini yang sebelumnya mungkin error
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. Bar Pencarian
                if (uiState.showSearch) {
                    item {
                        OutlinedTextField(
                            value = viewModel.searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            label = { Text(stringResource(R.string.search_budaya)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            singleLine = true
                        )
                    }
                }

                // 2. Tombol Navigasi Kategori (Opsional)
                item {
                    Button(
                        onClick = onKategoriClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(stringResource(R.string.browse_by_category))
                    }
                }

                // 3. Looping untuk Setiap Kategori yang Ditemukan
                groupedBudaya.forEach { (namaKategori, listBudaya) ->
                    // Judul Kategori (Tarian Adat, Makanan, dll)
                    item {
                        Text(
                            text = namaKategori,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Menampilkan Budaya dalam Grid 2 Kolom secara Manual di LazyColumn
                    val rows = listBudaya.chunked(2)
                    items(rows) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            for (budaya in rowItems) {
                                BudayaCardItem(
                                    budaya = budaya,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onBudayaClick(budaya.id) }
                                )
                            }
                            // Jika item ganjil, tambahkan ruang kosong di sebelahnya
                            if (rowItems.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudayaCardItem(
    budaya: Budaya,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            AsyncImage(
                model = budaya.gambar,
                contentDescription = budaya.namaBudaya,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = budaya.namaBudaya,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = budaya.asalDaerah ?: "Indonesia",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (budaya.ratingAvg != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⭐ ${String.format("%.1f", budaya.ratingAvg.toDouble())}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}