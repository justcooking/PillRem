import 'package:flutter/material.dart';

/// Clinical Palette Color Tokens matching PillRem design requirements
abstract class AppColors {
  // Light Theme Palette
  static const lightPrimary = Color(0xFF2B6CB0);     // Clinical Steel Blue
  static const lightBackground = Color(0xFFF0F4F8);  // Soft Clinical Tint
  static const lightSurface = Color(0xFFFFFFFF);     // Pure Crisp White
  static const lightText = Color(0xFF1A283B);        // Deep Slate Navy
  static const lightTextSecondary = Color(0xFF64748B);// Muted Slate
  static const lightSuccess = Color(0xFF388E67);     // Clinical Forest Green
  static const lightWarning = Color(0xFFD97706);     // Clinical Amber (Snoozed)
  static const lightDanger = Color(0xFFDC2626);      // Clinical Crimson (Missed)
  static const lightNeutral = Color(0xFF94A3B8);     // Cool Grey (Skipped)
  static const lightBorder = Color(0xFFE2E8F0);

  // Dark Theme Palette
  static const darkPrimary = Color(0xFF7AB2E8);      // Soft Sky Blue
  static const darkBackground = Color(0xFF0F1722);   // Deep Slate Navy
  static const darkSurface = Color(0xFF1E293B);      // Elevated Card Slate
  static const darkText = Color(0xFFEBF3FA);         // Soft Icy White
  static const darkTextSecondary = Color(0xFF94A3B8);// Muted Cool Grey
  static const darkSuccess = Color(0xFF68C496);      // Mint Emerald
  static const darkWarning = Color(0xFFF59E0B);      // Vibrant Amber (Snoozed)
  static const darkDanger = Color(0xFFF87171);       // Coral Red (Missed)
  static const darkNeutral = Color(0xFF64748B);      // Slate Grey (Skipped)
  static const darkBorder = Color(0xFF334155);
}

class AppTheme {
  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.light,
      colorScheme: const ColorScheme(
        brightness: Brightness.light,
        primary: AppColors.lightPrimary,
        onPrimary: Colors.white,
        secondary: AppColors.lightPrimary,
        onSecondary: Colors.white,
        error: AppColors.lightDanger,
        onError: Colors.white,
        surface: AppColors.lightSurface,
        onSurface: AppColors.lightText,
      ),
      scaffoldBackgroundColor: AppColors.lightBackground,
      cardTheme: CardTheme(
        color: AppColors.lightSurface,
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
          side: const BorderSide(color: AppColors.lightBorder, width: 1),
        ),
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.lightSurface,
        foregroundColor: AppColors.lightText,
        elevation: 0,
        scrolledUnderElevation: 0.5,
        centerTitle: false,
      ),
      dividerTheme: const DividerThemeData(
        color: AppColors.lightBorder,
        thickness: 1,
      ),
    );
  }

  static ThemeData get darkTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      colorScheme: const ColorScheme(
        brightness: Brightness.dark,
        primary: AppColors.darkPrimary,
        onPrimary: AppColors.darkBackground,
        secondary: AppColors.darkPrimary,
        onSecondary: AppColors.darkBackground,
        error: AppColors.darkDanger,
        onError: AppColors.darkBackground,
        surface: AppColors.darkSurface,
        onSurface: AppColors.darkText,
      ),
      scaffoldBackgroundColor: AppColors.darkBackground,
      cardTheme: CardTheme(
        color: AppColors.darkSurface,
        elevation: 0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(16),
          side: const BorderSide(color: AppColors.darkBorder, width: 1),
        ),
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: AppColors.darkSurface,
        foregroundColor: AppColors.darkText,
        elevation: 0,
        scrolledUnderElevation: 0.5,
        centerTitle: false,
      ),
      dividerTheme: const DividerThemeData(
        color: AppColors.darkBorder,
        thickness: 1,
      ),
    );
  }
}
