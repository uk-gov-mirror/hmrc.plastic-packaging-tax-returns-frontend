/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.plasticpackagingtax.returns.utils

case class TaxLiability(
  totalKgLiable: Long = 0,
  totalKgExempt: Long = 0,
  totalCredit: BigDecimal = 0,
  taxDue: BigDecimal = 0
)

object TaxLiabilityFactory extends PriceConverter {

  private val taxValueInPencePerKg = BigDecimal("0.20")

  def create(
    totalManufacturedKg: Long,
    totalImportedKg: Long,
    totalManufacturedKgBelowThreshold: Long,
    totalImportedKgBelowThreshold: Long,
    totalHumanMedicinesKg: Long,
    totalDirectExportsKg: Long,
    totalDirectExportsCreditPence: Long,
    totalConversionCreditPence: Long
  ): TaxLiability = {

    val totalKgAboveThreshold =
      (totalManufacturedKg + totalImportedKg) - (totalManufacturedKgBelowThreshold + totalImportedKgBelowThreshold)

    val totalKgLiable =
      (totalManufacturedKgBelowThreshold + totalImportedKgBelowThreshold) - (totalHumanMedicinesKg + totalDirectExportsKg)

    val totalKgExempt =
      totalKgAboveThreshold + (totalHumanMedicinesKg + totalDirectExportsKg)

    val totalCredit =
      totalDirectExportsCreditPence + totalConversionCreditPence

    val taxDue = taxValueInPencePerKg * totalKgLiable

    TaxLiability(totalKgLiable = totalKgLiable,
                 totalKgExempt = totalKgExempt,
                 totalCredit = toBigDecimal(totalCredit),
                 taxDue = format(taxDue)
    )
  }

}
