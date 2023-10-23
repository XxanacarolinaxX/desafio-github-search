package br.com.igorbag.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter(private val repositories: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    var carItemLister: (Repository) -> Unit = {}
    var btnShareLister: (Repository) -> Unit = {}

    // Cria uma nova view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    // Pega o conteúdo da view e troca pela informação do item da lista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]

        // Realizar o bind do viewHolder
        holder.nomeRepo.text = repository.name

        // Exemplo de click no item
        holder.itemView.setOnClickListener {
            carItemLister(repository)
        }

        // Exemplo de click no botão Share
        holder.btnShare.setOnClickListener {
            btnShareLister(repository)
        }
    }

    // Pega a quantidade de repositórios na lista
    override fun getItemCount(): Int = repositories.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeRepo = view.findViewById<TextView>(R.id.et_nome_usuario)
        val btnShare = view.findViewById<Button>(R.id.btn_confirmar)
    }
}
