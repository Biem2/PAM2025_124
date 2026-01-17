package com.Lokalan.ui.features.detail.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.Lokalan.R
import com.Lokalan.data.model.Review
import com.Lokalan.ui.features.detail.viewmodel.DetailViewModel
import com.Lokalan.ui.navigation.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    budayaId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Memuat detail budaya dan ulasan secara bersamaan
    LaunchedEffect(budayaId) {
        viewModel.loadBudayaDetail(budayaId)
        viewModel.loadReviews(budayaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.budaya_detail),
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.budaya == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.budaya_not_found),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Bagian Konten Utama Budaya
                    item {
                        DetailBudayaContent(uiState, viewModel, budayaId)
                    }

                    // Header Bagian Ulasan
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = stringResource(R.string.reviews),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }

                    // Logika Tampilan List Ulasan
                    if (uiState.reviews.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.no_reviews_yet),
                                modifier = Modifier.fillMaxWidth().padding(32.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        items(uiState.reviews) { review ->
                            ReviewItem(review = review)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailBudayaContent(uiState: com.Lokalan.ui.features.detail.viewmodel.DetailUiState, viewModel: DetailViewModel, budayaId: Int) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = uiState.budaya?.gambar,
            contentDescription = uiState.budaya?.namaBudaya,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = uiState.budaya?.namaBudaya ?: "",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = uiState.budaya?.asalDaerah ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB400))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${uiState.budaya?.ratingAvg ?: 0.0}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "(${uiState.budaya?.totalReviews ?: 0} ulasan)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(text = uiState.budaya?.deskripsi ?: "", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Input Review (Hanya jika login dan belum pernah review)
        if (uiState.isUserLoggedIn && !uiState.hasUserRated) {
            ReviewInputSection(viewModel, budayaId, uiState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewInputSection(viewModel: DetailViewModel, budayaId: Int, uiState: com.Lokalan.ui.features.detail.viewmodel.DetailUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.give_rating), fontWeight = FontWeight.Bold)
            Row {
                for (i in 1..5) {
                    IconButton(onClick = { viewModel.setRating(i) }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (i <= uiState.userRating) Color(0xFFFFB400) else Color.Gray
                        )
                    }
                }
            }
            OutlinedTextField(
                value = viewModel.comment,
                onValueChange = { viewModel.updateComment(it) },
                label = { Text(stringResource(R.string.comment_optional)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.submitReview(budayaId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.userRating > 0
            ) {
                Text(stringResource(R.string.submit_review))
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar (Inisial Nama)
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.user?.nama?.firstOrNull()?.toString()?.uppercase() ?: "?",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = review.user?.nama ?: "Anonim", fontWeight = FontWeight.Bold)
                    review.createdAt?.let { Text(text = it, style = MaterialTheme.typography.labelSmall) }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Rating Bintang
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (index < review.nilai) Color(0xFFFFB400) else Color.Gray
                        )
                    }
                }
            }
            if (!review.komentar.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = review.komentar, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}