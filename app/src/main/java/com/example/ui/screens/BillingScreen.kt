package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreditPackage
import com.example.data.model.SubscriptionPlan
import com.example.ui.theme.*
import com.example.ui.viewmodel.VideoCreatorViewModel

@Composable
fun BillingScreen(
    viewModel: VideoCreatorViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val plans = viewModel.plans
    val creditPacks = viewModel.creditPacks

    var selectedPackForPayment by remember { mutableStateOf<CreditPackage?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Orange Money") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ElectricViolet.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Solde de crédits actif", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${userProfile.creditBalance} crédits",
                            color = TextPrimary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Surface(
                            color = EmeraldSuccessBg,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Formule Pro Active",
                                color = EmeraldSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "1 crédit = 1 seconde de traitement audio ou 0.1 scène vidéo HD.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Credit Top-Up Packs
        item {
            Text(
                text = "Recharge directe de crédits",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Paiement instantané par Mobile Money (Orange, MTN, Wave) ou Carte Bancaire.",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        items(creditPacks) { pack ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedPackForPayment = pack }
                    .testTag("credit_pack_${pack.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (pack.isBestValue) ElectricViolet.copy(alpha = 0.15f) else StudioCardBg
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (pack.isBestValue) ElectricVioletLight else StudioBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "+${pack.credits} crédits",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (pack.bonusCredits > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = AmberWarningBg,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "+${pack.bonusCredits} offerts",
                                        color = AmberWarning,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${pack.priceCfa} (${pack.priceEur})",
                            color = NeonCyanLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { selectedPackForPayment = pack },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Acheter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Subscription Plans
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Formules d'abonnements",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Économisez jusqu'à 40% sur le coût par vidéo avec une formule mensuelle.",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        items(plans) { plan ->
            val isCurrentPlan = plan.id == userProfile.planId
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (plan.isPopular) ElectricVioletLight else StudioBorder
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = plan.name, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        if (plan.badgeLabel != null) {
                            Surface(
                                color = ElectricViolet,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = plan.badgeLabel,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${plan.priceMonthlyCfa} / mois (${plan.priceMonthlyEur})",
                        color = NeonCyanLight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    plan.features.forEach { feat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = feat, color = TextSecondary, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.subscribePlan(plan) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrentPlan) StudioCardElevated else ElectricViolet
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        enabled = !isCurrentPlan
                    ) {
                        Text(
                            text = if (isCurrentPlan) "Formule actuelle" else "Choisir cette formule",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Transactions History
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Historique des transactions",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(transactions) { tx ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = tx.transactionType, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = tx.description, color = TextMuted, fontSize = 11.sp)
                    }
                    Text(
                        text = if (tx.amount > 0) "+${tx.amount}" else "${tx.amount}",
                        color = if (tx.amount > 0) EmeraldSuccess else RoseError,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Payment Dialog for Selected Pack
    selectedPackForPayment?.let { pack ->
        AlertDialog(
            onDismissRequest = { selectedPackForPayment = null },
            title = {
                Text(
                    text = "Acheter ${pack.credits} crédits (${pack.priceCfa})",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Choisissez votre moyen de paiement sécurisé :",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    val paymentMethods = listOf("Orange Money", "MTN MoMo", "Wave", "Carte Bancaire")
                    paymentMethods.forEach { method ->
                        Surface(
                            color = if (selectedPaymentMethod == method) ElectricViolet.copy(alpha = 0.25f) else StudioCardBg,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (selectedPaymentMethod == method) ElectricVioletLight else StudioBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = method }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = method,
                                    color = if (selectedPaymentMethod == method) Color.White else TextSecondary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                                if (selectedPaymentMethod == method) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyanLight, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.purchaseCredits(pack, selectedPaymentMethod)
                        selectedPackForPayment = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text("Valider le paiement (${pack.priceCfa})")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPackForPayment = null }) {
                    Text("Annuler", color = TextSecondary)
                }
            },
            containerColor = StudioDarkSurface
        )
    }
}
