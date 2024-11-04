import "./globals.css";
import { Inter } from "next/font/google";
import Navbar from "./components/navbar";
import { AuthProvider } from "@/lib/auth";

const inter = Inter({ subsets: ["latin"] });

export const metadata = {
  title: "IDentify",
  description: "The website to identify your unknown objects.",
};

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body
        className={`${inter.className} bg-gray-900 text-gray-100 min-h-screen`}
      >
        <AuthProvider>
          <Navbar />
          {children}
        </AuthProvider>
      </body>
    </html>
  );
}
