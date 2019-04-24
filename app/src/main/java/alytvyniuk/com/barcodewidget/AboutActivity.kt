package alytvyniuk.com.barcodewidget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_about.*
import java.lang.IllegalArgumentException

class AboutActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        fun intent(context: Context) = Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        linkToLinkedIn.setOnClickListener(this)
        linkToGithub.setOnClickListener(this)
        linkToProject.setOnClickListener(this)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View) {
        val uri = when (v.id) {
            R.id.linkToLinkedIn -> "https://www.linkedin.com/in/andrii-lytvyniuk-aba28886/"
            R.id.linkToGithub -> "https://github.com/AndriiLytvyniuk"
            R.id.linkToProject -> "https://github.com/AndriiLytvyniuk/BarcodeWidget"
            else -> throw IllegalArgumentException("Unknown view with id: ${v.id}")
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(browserIntent)
    }
}
