package br.com.igorbag.githubsearch.ui

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var nomeUsuario: EditText
    lateinit var btnConfirmar: Button
    lateinit var listaRepositories: RecyclerView
    lateinit var githubApi: GitHubService
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
        showUserName()
        setupRetrofit()
        setupListeners()
        getAllReposByUserName()
    }

    // Metodo responsavel por realizar o setup da view e recuperar os Ids do layout
    fun setupView() {
        nomeUsuario = findViewById(R.id.et_nome_usuario)
        btnConfirmar = findViewById(R.id.btn_confirmar)
        listaRepositories = findViewById(R.id.rv_lista_repositories)
    }

    // Metodo responsavel por configurar os listeners click da tela
    private fun setupListeners() {
        // @TODO 2 - Colocar a acao de click do botao confirmar
        btnConfirmar.setOnClickListener {
            saveUserLocal()
        }
    }

    // Metodo responsavel por persistir o usuario preenchido na EditText com SharedPreferences
    private fun saveUserLocal() {
        val userName = nomeUsuario.text.toString()
        val editor = sharedPreferences.edit()
        editor.putString("username", userName)
        editor.apply()
        showUserName()
    }

    private fun showUserName() {
        // @TODO 4 - Após persistir o usuário, exibir sempre as informações no EditText
        // Se a SharedPreferences possui algum valor, exibir no próprio EditText o valor salvo
        val savedUserName = sharedPreferences.getString("username", "")
        nomeUsuario.setText(savedUserName)
    }

    // Metodo responsavel por fazer a configuracao base do Retrofit
    fun setupRetrofit() {
        // Configurar Retrofit com a URL base da API do GitHub
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/") // Defina a URL base correta
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crie uma instância de GitHubService com o Retrofit configurado
        githubApi = retrofit.create(GitHubService::class.java)
    }

    // Metodo responsavel por buscar todos os repositorios do usuario fornecido
    fun getAllReposByUserName() {
        // @TODO 6 - Realizar a implementação do callback do Retrofit e chamar o método setupAdapter se retornar os dados com sucesso
        val userName = nomeUsuario.text.toString()
        githubApi.getReposByUser(userName).enqueue(object : Callback<List<Repository>> {
            override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {
                if (response.isSuccessful) {
                    val repositoryList = response.body()
                    repositoryList?.let {
                        setupAdapter(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                // Tratar falha na chamada à API
            }
        })
    }

    // Metodo responsavel por realizar a configuracao do adapter
    fun setupAdapter(list: List<Repository>) {
        // @TODO 7 - Implementar a configuração do Adapter, construir o adapter e instanciá-lo
        // Passando a listagem dos repositórios
        val adapter = RepositoryAdapter(list)
        listaRepositories.adapter = adapter
    }

    // @TODO 11 - Colocar esse método no clique do item "share" do adapter
    fun shareRepositoryLink(urlRepository: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // @TODO 12 - Colocar esse método no clique do item no adapter
    fun openBrowser(urlRepository: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlRepository))
        startActivity(intent)
    }
}
