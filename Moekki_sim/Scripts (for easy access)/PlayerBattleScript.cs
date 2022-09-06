using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;



public class PlayerBattleScript : MonoBehaviour
{   
    public TextMeshProUGUI healthDisplay;
    public float health;

    private float maxHealth;
    private float realHealth;
    public bool isDead = false;
    // Start is called before the first frame update
    void Start()
    {
        maxHealth = health;
    }

    // Update is called once per frame
    void Update()
    {
        realHealth = (health/maxHealth)*100;
        
        if(healthDisplay != null){
            healthDisplay.SetText(""+realHealth/*.ToString("#")*/+"%");
        }
        else{
            healthDisplay.SetText("");
        }
        if(isDead){
            Kill();
        }
    }

    private void Kill(){
        Destroy(gameObject);
    }

    public void TakeDamage(float damage){
        health -= damage;

        if(health < 0){
            isDead = true;
        }
    }
}
